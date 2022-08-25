package com.sparta.springassignment01.service;

import com.sparta.springassignment01.dto.CommentRequestDto;
import com.sparta.springassignment01.dto.CommentResponseDto;
import com.sparta.springassignment01.dto.ResponseDto;
import com.sparta.springassignment01.entity.Comment;
import com.sparta.springassignment01.entity.Member;
import com.sparta.springassignment01.entity.Post;
import com.sparta.springassignment01.jwt.TokenProvider;
import com.sparta.springassignment01.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;



    @Transactional
    public Member ValidateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }



    // 댓글 생성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = ValidateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");

        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .writer(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );

    }


        //댓글 조회
        @Transactional(readOnly = true)
        public ResponseDto<?> getAllCommentsByPost(Long postId) {
            Post post = postService.isPresentPost(postId);
            if (null == post) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
            }

            List<Comment> commentList = commentRepository.findAllByPost(post);
            List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

            for (Comment comment : commentList) {

                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .id(comment.getId())
                                .writer(comment.getMember().getNickname())
                                .content(comment.getContent())
                                .createAt(comment.getCreatedAt())
                                .modifiedAt(comment.getModifiedAt())
                                .build()
                );
            }
            return ResponseDto.success(commentResponseDtoList);
        }


        //댓글 수정
        @Transactional
        public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
            if (null == request.getHeader("Refresh-Token")) {
                return ResponseDto.fail("MEMBER_NOT_FOUND",
                        "로그인이 필요합니다.");
            }

            if (null == request.getHeader("Authorization")) {
                return ResponseDto.fail("MEMBER_NOT_FOUND",
                        "로그인이 필요합니다.");
            }

            Member member = ValidateMember(request);
            if (null == member) {
                return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
            }

            Post post = postService.isPresentPost(requestDto.getPostId());
            if (null == post) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
            }

            Comment comment = isPresentComment(id);
            if (null == comment) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
            }

            if (comment.validateMember(member)) {
                return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
            }

            comment.update(requestDto);
            return ResponseDto.success(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .writer(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .createAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }


        //댓글 삭제
        @Transactional
        public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
            if (null == request.getHeader("Refresh-Token")) {
                return ResponseDto.fail("MEMBER_NOT_FOUND",
                        "로그인이 필요합니다.");
            }

            if (null == request.getHeader("Authorization")) {
                return ResponseDto.fail("MEMBER_NOT_FOUND",
                        "로그인이 필요합니다.");
            }

            Member member = ValidateMember(request);
            if (null == member) {
                return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
            }

            Comment comment = isPresentComment(id);
            if (null == comment) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
            }

            if (comment.validateMember(member)) {
                return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
            }

            commentRepository.delete(comment);
            return ResponseDto.success("success");
        }



}


