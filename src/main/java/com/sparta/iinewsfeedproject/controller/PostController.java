package com.sparta.iinewsfeedproject.controller;

import com.sparta.iinewsfeedproject.dto.PostPagingResponseDto;
import com.sparta.iinewsfeedproject.dto.PostRequestDto;
import com.sparta.iinewsfeedproject.dto.PostResponseDto;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto, HttpServletRequest request) {
        User user = getUser(request);
        PostResponseDto post = postService.createPost(postRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findOnePost(@PathVariable Long id) {
        PostResponseDto responseDto = postService.findOnePost(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<PostPagingResponseDto>> findAllPosts(@RequestParam(defaultValue = "1",value = "page")int page, @RequestParam(defaultValue = "10",value = "size")int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC,"updatedAt");
        List<PostPagingResponseDto > postPagingResponseDtoList = postService.findAllPosts(pageable);
        return ResponseEntity.ok(postPagingResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyPost(@PathVariable Long id, @RequestBody @Valid PostRequestDto postRequestDto, HttpServletRequest request) {
        User user = getUser(request);
        postService.modifyPost(id,postRequestDto,user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, HttpServletRequest request) {
        User user = getUser(request);
        postService.deletePost(id,user);
        return ResponseEntity.noContent().build();
    }

    public User getUser(HttpServletRequest request) {
        return (User)request.getAttribute("user");
    }
}
