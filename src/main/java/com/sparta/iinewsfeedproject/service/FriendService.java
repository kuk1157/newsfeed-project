package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.exception.FriendNotFoundException;
import com.sparta.iinewsfeedproject.repository.FriendRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Transactional
    public void deleteFriend(Long fromUserId, Long userId) {
        if (!friendRepository.existsByFromUserIdAndUserId(fromUserId, userId)) {
            throw new FriendNotFoundException("존재하지 않는 친구 관계입니다.");
        }
        friendRepository.deleteByFromUserIdAndUserId(fromUserId, userId);
    }
}
