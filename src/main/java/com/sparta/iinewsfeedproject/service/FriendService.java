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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteFriend(Long friendId, Long userId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FRIEND));

        if (!friend.getFromUser().getId().equals(userId) && !friend.getToUserId().equals(userId)) {
            throw  new CustomException(ErrorCode.NOT_FORBIDDEN);
        }

        friendRepository.delete(friend);
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
        // 요청 보낼때 - 로그인 id == from_user_id
        Long toUserId = requestDto.getToUserId();

        if (Objects.equals(toUserId, fromUser.getId())) {
            throw new CustomException(ErrorCode.NOT_MY_FRIEND_REQUEST);
        }

        Optional<User> fromUserId = userRepository.findById(toUserId);
        if (fromUserId.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        int count = friendRepository.findByToUserIdAndStatus(toUserId, fromUser.getId());
        int allCount = friendRepository.findAllById();
        int distinct = friendRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId);
        // 내가 요청한 사람이 이미 나에게 친구요청한 경우
        if (distinct != 0 && allCount != 0) {
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
        }
        // 내가 이미 요청을 보냈을 경우 그리고 상태값이 대기와 승인일 경우
        if (count != 0 && allCount != 0) {
            throw new CustomException(ErrorCode.DUPLICATE_FRIEND_REQUEST);
        }

        Friend friend = new Friend(requestDto.getToUserId(), requestDto.getStatus(), fromUser);
        Friend saveFriends = friendRepository.save(friend);
        return new FriendResponseDto(saveFriends);
    }

    public List<FriendResponseDto> getFriends(User toUser) {
        // 요청 조회시 - 로그인 id == to_user_id
        String status = "PENDING"; // 대기중 상태값의 친구요청만 조회
        return friendRepository.findByToUserIdOrFromUserIdAndStatus(toUser.getId(), toUser.getId(), status).stream().map(FriendResponseDto::new).toList();
    }

    @Transactional
    public Long updateFriend(Long friendId, FriendRequestDto requestDto, User toUser) {

        Friend friend = findFriend(friendId);

        // 요청 응답시 - 로그인 id == to_user_id
        int checkId = friendRepository.findByToUserIdAndStatusAndId(toUser.getId(), "PENDING", friendId);
        if (checkId == 0) {
            throw new CustomException(ErrorCode.NOT_MY_RESPONSE);
        }

        String status = requestDto.getStatus();
        if (status.equals("ACCEPT") || status.equals("REJECT")) {
            String statusCheck = friendRepository.findAllByStatus(friendId);
            if (statusCheck.equals("PENDING")) {
                friend.update(status);
            } else {
                throw new CustomException(ErrorCode.NOT_RESPONSE);
            }
        } else {
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
