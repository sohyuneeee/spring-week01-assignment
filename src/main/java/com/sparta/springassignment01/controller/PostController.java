package com.sparta.springassignment01.controller;

import com.sparta.springassignment01.dto.PasswordDto;
import com.sparta.springassignment01.dto.PostRequestDto;
import com.sparta.springassignment01.dto.ResponseDto;
import com.sparta.springassignment01.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    //전체 게시글 목록 조회
    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    //게시글 조회
    @GetMapping("/api/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    //게시글 작성
    @PostMapping("/api/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    //게시글 비밀번호 확인
    @PostMapping("/api/post/{id}")
    public ResponseDto<?> checkPassword(@PathVariable Long id, @RequestBody PasswordDto password) {
        return postService.checkPassword(id, password);
    }


    //게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }


    //게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }


}
