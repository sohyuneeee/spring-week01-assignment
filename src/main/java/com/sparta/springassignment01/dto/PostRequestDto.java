package com.sparta.springassignment01.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PostRequestDto {
    private final String title;
    private final String writer;
    private final String password;
    private final String content;


}