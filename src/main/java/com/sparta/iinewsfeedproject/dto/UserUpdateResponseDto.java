package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserUpdateResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserUpdateResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = LocalDateTime.now();
    }
}