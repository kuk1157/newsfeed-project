package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private int code;
    private String message;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.code = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
