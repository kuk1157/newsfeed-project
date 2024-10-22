package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.Friend;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendResponseDto {
    private Long id;
    private String status;
    private UserResponseDto fromUser;

    public FriendResponseDto(Friend friend) {
        this.id = friend.getId();
        // 추후 로그인과 함께 확인
        // this.fromUser = new UserResponseDto(friend.getFromUser());
        this.status = friend.getStatus();
    }

}
