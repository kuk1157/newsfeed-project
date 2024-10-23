package com.sparta.iinewsfeedproject.exception;

import com.sparta.iinewsfeedproject.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        return throwException(e.errorCode, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> validException(MethodArgumentNotValidException e) {
        return throwException(ErrorCode.valueOf(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()), e);
    }

    private ResponseEntity<ErrorResponseDto> throwException(ErrorCode errorCode, Exception e) {
        log.error("stackTrace={}",e.getStackTrace());
        log.error("CustomExceptionHandler",e);
        ErrorResponseDto error = new ErrorResponseDto(errorCode);
        return ResponseEntity.status(error.getCode()).body(error);
    }
}
