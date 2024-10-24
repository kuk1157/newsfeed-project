package com.sparta.iinewsfeedproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordRequestDto {
    private String pastPassword;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "비밀번호는 문자, 숫자, 기호를 포함해 8자 이상으로 기입해 주십시오.")
    private String newPassword;
}
