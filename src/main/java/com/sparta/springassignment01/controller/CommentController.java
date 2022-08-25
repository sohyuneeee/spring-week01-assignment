package com.sparta.springassignment01.controller;

import com.sparta.springassignment01.dto.CommentRequestDto;
import com.sparta.springassignment01.dto.ResponseDto;
import com.sparta.springassignment01.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //댓글 생성
    @PostMapping("api/auth/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    //댓글 조회
    @GetMapping("api/comment/{id}")
    public ResponseDto<?> getAllComments(@PathVariable Long id) {
        return commentService.getAllCommentsByPost(id);
    }

    //댓글 수정
    @PutMapping("api/auth/comment/{id}")
    public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(id,requestDto,request);
    }

    @DeleteMapping("api/auth/comment/{id}")
    public ResponseDto<?> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id,request);
    }

}
