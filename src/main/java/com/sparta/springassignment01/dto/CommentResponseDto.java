package com.sparta.springassignment01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
