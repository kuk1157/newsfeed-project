package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String email;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
