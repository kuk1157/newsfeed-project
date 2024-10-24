package com.sparta.iinewsfeedproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignupRequestDto {
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "INCORRECT_EMAIL_FORMAT")
    private String email;
    @NotBlank
    private String name;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "INCORRECT_PASSWORD_FORMAT")
    private String password;
}
