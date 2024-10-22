package com.sparta.iinewsfeedproject.dto;

import com.sparta.iinewsfeedproject.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostPagingResponseDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final UserResponseDto writer;
    private final int size;
    private final int page;
    private final int totalPages;
    private final Long totalElements;

    public PostPagingResponseDto(Post post, int page, int size, int totalPages, Long totalElements) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.writer = new UserResponseDto(post.getUser());
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
