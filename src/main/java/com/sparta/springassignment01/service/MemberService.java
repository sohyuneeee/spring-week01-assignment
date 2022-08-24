package com.sparta.springassignment01.service;

import com.sparta.springassignment01.dto.*;
import com.sparta.springassignment01.entity.Member;
import com.sparta.springassignment01.jwt.TokenProvider;
import com.sparta.springassignment01.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    @Transactional
    public ResponseDto<?> register(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME","중복된 닉네임 입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_CORRECT", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
       Member member = isPresentMember(requestDto.getNickname());
       if (null == member) {
           return ResponseDto.fail("MEMBER_NOT_FOUND","사용자를 찾을 수 없습니다.");
       }

       if (!member.validatePassword(passwordEncoder, requestDto.getPassword()))
           return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");


        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );

    }


    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }


}
