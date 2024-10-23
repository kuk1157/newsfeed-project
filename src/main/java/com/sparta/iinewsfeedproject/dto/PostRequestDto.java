package com.sparta.iinewsfeedproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank(message = "NULL_CONTENT")
    private String content;
}
