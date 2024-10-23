package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Post;
import com.sparta.iinewsfeedproject.exception.CustomException;
import com.sparta.iinewsfeedproject.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findOnePost(Long id){
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));
    }
}
