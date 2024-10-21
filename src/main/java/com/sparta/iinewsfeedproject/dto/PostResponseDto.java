package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final UserResponseDto writer;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.writer = new UserResponseDto(post.getUser());
    }
}
