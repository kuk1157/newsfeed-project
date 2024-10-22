package com.sparta.iinewsfeedproject.dto;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    private Long id;
    private Long fromUserId; // 임시 향후 jwt 대체
    private Long toUserId;
    private String status;
}
