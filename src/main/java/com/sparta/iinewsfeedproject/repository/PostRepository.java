package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findOnePost(Long id){
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 번호입니다."));
    }
}
