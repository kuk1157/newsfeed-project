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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void deleteFriend(Long fromUserId, Long userId) {
        if (!friendRepository.existsByFromUserIdAndToUserId(fromUserId, userId)) {
            throw new FriendNotFoundException("존재하지 않는 친구 관계입니다.");
            // 이건없어서그렇고 파일
        }
    }

    public FriendResponseDto createFriend(FriendRequestDto requestDto, User fromUser) {
        Long toUserId = requestDto.getToUserId();
        getAcceptedFriends(toUserId);
        int count = friendRepository.findByToUserIdAndStatus(toUserId);
        int allCount = friendRepository.findAllById();
        if(count != 0 && allCount != 0){
            throw new IllegalArgumentException("친구 요청을 이미 보낸 회원입니다.");
        }
        friendRepository.deleteByFromUserIdAndToUserId(fromUserId, userId);
        Friend friend = new Friend(requestDto.getToUserId(), requestDto.getStatus(), fromUser);
        Friend saveFriends = friendRepository.save(friend);
        FriendResponseDto friendResponseDto = new FriendResponseDto(saveFriends);
        return friendResponseDto;
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
