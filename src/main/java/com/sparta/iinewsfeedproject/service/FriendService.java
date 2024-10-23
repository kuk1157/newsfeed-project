package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.dto.FriendDto;
import com.sparta.iinewsfeedproject.dto.FriendRequestDto;
import com.sparta.iinewsfeedproject.dto.FriendResponseDto;
import com.sparta.iinewsfeedproject.entity.Friend;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.CustomException;
import com.sparta.iinewsfeedproject.exception.ErrorCode;
import com.sparta.iinewsfeedproject.repository.FriendRepository;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new CustomException(ErrorCode.NOT_FOUND_FRIEND);
        }
        friendRepository.deleteByFromUserIdAndToUserId(fromUserId, userId);
    }


    public List<FriendDto> getAcceptedFriends(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        List<Friend> friends = friendRepository.findByFromUserIdAndStatusOrToUserIdAndStatus(userId, "ACCEPT", userId, "ACCEPT");
        return friends.stream()
                .map(friend -> {
                    Long otherUserId = friend.getFromUser().getId().equals(userId) ? friend.getToUserId() : friend.getFromUser().getId();
                    User otherUser = userRepository.findById(otherUserId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
                    return new FriendDto(otherUserId, otherUser.getName(), otherUser.getEmail());
                })
                .collect(Collectors.toList());
    }

    public FriendResponseDto createFriend(FriendRequestDto requestDto, User fromUser) {
        // 중복 체크 추가해야함 내일 넣을예정
        Long toUserId = requestDto.getToUserId();
        Optional<User> fromUserId = userRepository.findById(toUserId);
        if (fromUserId.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        int count = friendRepository.findByToUserIdAndStatus(toUserId);
        int allCount = friendRepository.findAllById();
        if(count != 0 && allCount != 0){
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
        }
        Friend friend = new Friend(requestDto.getToUserId(), requestDto.getStatus(), fromUser);
        Friend saveFriends = friendRepository.save(friend);
        FriendResponseDto friendResponseDto = new FriendResponseDto(saveFriends);
        return friendResponseDto;
    }

    public List<FriendResponseDto> getFriends() {
        String status = "PENDING"; // 대기중 상태값의 친구요청만 조회
        return friendRepository.findByStatus(status).stream().map(FriendResponseDto::new).toList();
    }

    @Transactional
    public Long updateFriend(Long friendId, FriendRequestDto requestDto) {
        Friend friend = findFriend(friendId);
        String status = requestDto.getStatus();

        if(status.equals("ACCEPT") || status.equals("REJECT")){
            String statusCheck = friendRepository.findAllByStatus(friendId);
            if(statusCheck.equals("PENDING")){
                friend.update(status);
            }else{
                throw new CustomException(ErrorCode.NOT_RESPONSE);
            }
        }else{
            throw new CustomException(ErrorCode.NOT_VALID_STATUS);
        }
        return friendId;
    }


    private Friend findFriend(Long friendId) {
        return friendRepository.findById(friendId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_ACCESS)
        );
    }
}
