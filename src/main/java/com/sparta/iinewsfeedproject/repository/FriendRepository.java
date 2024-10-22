package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 친구 관계가 존재하는지 확인
    boolean existsByFromUserIdAndUserId(Long fromUserId, Long userId);

    // 특정 친구 관계 삭제
    void deleteByFromUserIdAndUserId(Long fromUserId, Long userId);
}
