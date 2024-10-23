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
        // 저희가 RequestDto 에 걸어둔 예외 메세지를 ErrorCode 에 있는 상수명으로 바꿔주세요.
        // ex -> (message = "이메일 형식이 아닙니다." -> message = "INCORRECT_EMAIL_FORMAT")
        // 아래에서 쓴 getDefaultMessage 가 저희가 dto validation 에 적어둔 메세지를 반환하는 메서드인데
        // ErrorCode.valueOf(String name) 은 name 과 일치하는 상수를 반환하기 때문에 이름을 "이메일 형식입니다" 라고 지정해둔 상수는 없기 때문에
        // 지정해둔 상수명으로 바꿔주시면 될 것 같습니다.
        // 제가 바꾸는것도 좋을 것 같지만 파트마다 예외처리 다 바꿔두면 충돌이 많이 날 것 같아서 이렇게 적어둡니다.
        return throwException(ErrorCode.valueOf(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()), e);
    }

    private ResponseEntity<ErrorResponseDto> throwException(ErrorCode errorCode, Exception e) {
        // 왜 예외가 났는지 서버에서도 알수 있게 하기 위해서 log.error 사용했습니다.
        log.error("stackTrace={}",e.getStackTrace());
        log.error("CustomExceptionHandler",e);
        ErrorResponseDto error = new ErrorResponseDto(errorCode);
        return ResponseEntity.status(error.getCode()).body(error);
    }
}
