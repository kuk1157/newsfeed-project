package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.dto.PostRequestDto;
import com.sparta.iinewsfeedproject.dto.PostResponseDto;
import com.sparta.iinewsfeedproject.entity.Post;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto.getContent(),user);
        return new PostResponseDto(postRepository.save(post));
    }
}
