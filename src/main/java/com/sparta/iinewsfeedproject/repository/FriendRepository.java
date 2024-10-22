package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 친구 관계가 존재하는지 확인
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long userId);

    // 특정 친구 관계 삭제
    void deleteByFromUserIdAndToUserId(Long fromUserId, Long userId);

    // 특정 사용자의 친구 목록 중 수락된 친구만 조회
    List<Friend> findByFromUserIdAndStatus(Long fromUserId, String status);
}
