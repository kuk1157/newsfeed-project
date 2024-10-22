package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.dto.FriendDto;
import com.sparta.iinewsfeedproject.entity.Friend;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.FriendNotFoundException;
import com.sparta.iinewsfeedproject.exception.UserNotFoundException;
import com.sparta.iinewsfeedproject.repository.FriendRepository;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void deleteFriend(Long fromUserId, Long userId) {
        if (!friendRepository.existsByFromUserIdAndToUserId(fromUserId, userId)) {
            throw new FriendNotFoundException("존재하지 않는 친구 관계입니다.");
        }
        friendRepository.deleteByFromUserIdAndToUserId(fromUserId, userId);
    }

    public List<FriendDto> getAcceptedFriends(Long userId) {
        Optional<User> fromUser = userRepository.findById(userId);
        if (fromUser.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 유저번호 입니다.");
        }

        List<Friend> friends = friendRepository.findByFromUserIdAndStatus(userId, "ACCEPT");
        return friends.stream()
                .map(friend -> {
                    Optional<User> toUser = userRepository.findById(friend.getToUserId());
                    return new FriendDto(friend.getToUserId(), toUser.get().getName(), toUser.get().getEmail());
                })
                .collect(Collectors.toList());
    }
}
