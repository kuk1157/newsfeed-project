package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String name;
    private String email;

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
