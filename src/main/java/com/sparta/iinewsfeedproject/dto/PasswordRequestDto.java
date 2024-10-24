package com.sparta.iinewsfeedproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordRequestDto {
    private String pastPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "INCORRECT_PASSWORD_FORMAT")
    private String newPassword;
}
