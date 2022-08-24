package com.sparta.springassignment01.controller;

import com.sparta.springassignment01.dto.LoginRequestDto;
import com.sparta.springassignment01.dto.MemberRequestDto;
import com.sparta.springassignment01.dto.ResponseDto;
import com.sparta.springassignment01.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class MemberController {

        private final MemberService memberService;

        //회원가입
        @PostMapping("api/member/signup")
        public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
            return memberService.register(requestDto);
        }

        //로그인
        @PostMapping("api/member/login")
        public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
            return memberService.login(requestDto, response);
        }

        @PostMapping( "/api/auth/member/logout")
        public ResponseDto<?> logout(HttpServletRequest request) {
            return memberService.logout(request);
        }

    }

