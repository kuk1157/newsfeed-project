package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.dto.PostPagingResponseDto;
import com.sparta.iinewsfeedproject.dto.PostRequestDto;
import com.sparta.iinewsfeedproject.dto.PostResponseDto;
import com.sparta.iinewsfeedproject.entity.Post;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto.getContent(),user);
        return new PostResponseDto(postRepository.save(post));
    }

    public PostResponseDto findOnePost(Long id) {
        Post post = postRepository.findOnePost(id);
        return new PostResponseDto(post);
    }

    public List<PostPagingResponseDto > findAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream()
                .map(post -> new PostPagingResponseDto(post,pageable.getPageNumber()+1,pageable.getPageSize(),posts.getTotalPages(),posts.getTotalElements()))
                .toList();
    }
}
