package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.Friend;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendResponseDto {
    private Long id;
    private Long toUserId;
    private String status;
    private UserResponseDto fromUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public FriendResponseDto(Friend friend) {
        this.id = friend.getId();
        // 추후 로그인과 함께 확인
        // this.fromUser = new UserResponseDto(friend.getFromUser());
        this.toUserId = friend.getToUserId();
        this.status = friend.getStatus();
        this.createdAt = friend.getCreatedAt();
        this.updatedAt = friend.getUpdatedAt();
    }
}
