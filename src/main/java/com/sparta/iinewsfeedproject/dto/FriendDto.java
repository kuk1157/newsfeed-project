package com.sparta.iinewsfeedproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class FriendDto {
    private Long userId;
    private String name;
    private String email;
}

