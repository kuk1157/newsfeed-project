package com.sparta.iinewsfeedproject.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class ErrorResponseDto {
    private int code;
    private String message;
}
