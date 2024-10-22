package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.Friend;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendResponseDto {
    private Long id;
    private Long toUserId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FriendResponseDto(Friend friend) {
        this.id = friend.getId();
        this.toUserId = friend.getToUserId();
        this.status = friend.getStatus();
        this.createdAt = friend.getCreatedAt();
        this.updatedAt = friend.getUpdatedAt();
    }

}
