package com.sparta.iinewsfeedproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
