package com.sparta.iinewsfeedproject.dto;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    private Long id;
    private Long toUserId;
    private String status;
}
