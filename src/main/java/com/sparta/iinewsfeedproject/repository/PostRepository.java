package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
