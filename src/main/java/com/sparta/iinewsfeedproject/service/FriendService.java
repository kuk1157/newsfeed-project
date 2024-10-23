package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.dto.FriendDto;
import com.sparta.iinewsfeedproject.dto.FriendRequestDto;
import com.sparta.iinewsfeedproject.dto.FriendResponseDto;
import com.sparta.iinewsfeedproject.entity.Friend;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.FriendNotFoundException;
import com.sparta.iinewsfeedproject.exception.UserNotFoundException;
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
            throw new FriendNotFoundException("존재하지 않는 친구 관계입니다.");
        }
        friendRepository.deleteByFromUserIdAndToUserId(fromUserId, userId);
    }


    public List<FriendDto> getAcceptedFriends(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 유저번호 입니다.");
        }

        List<Friend> friends = friendRepository.findByFromUserIdAndStatusOrToUserIdAndStatus(userId, "ACCEPT", userId, "ACCEPT");
        return friends.stream()
                .map(friend -> {
                    Long otherUserId = friend.getFromUser().getId().equals(userId) ? friend.getToUserId() : friend.getFromUser().getId();
                    User otherUser = userRepository.findById(otherUserId)
                            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저번호 입니다: " + otherUserId));
                    return new FriendDto(otherUserId, otherUser.getName(), otherUser.getEmail());
                })
                .collect(Collectors.toList());
    }

    public FriendResponseDto createFriend(FriendRequestDto requestDto, User fromUser) {
        // 요청 보낼때 - 로그인 id == from_user_id
        Long toUserId = requestDto.getToUserId();

        Optional<User> fromUserId = userRepository.findById(toUserId);
        if (fromUserId.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 유저번호 입니다.");
        }

        int count = friendRepository.findByToUserIdAndStatus(toUserId, fromUser.getId());
        int allCount = friendRepository.findAllById();
        int distinct = friendRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId);
        // 내가 요청한 사람이 이미 나에게 친구요청한 경우
        if(distinct != 0 && allCount != 0){
            throw new IllegalArgumentException("친구 요청을 이미 보낸 회원입니다.");
        }
        // 내가 이미 요청을 보냈을 경우 그리고 상태값이 대기와 승인일 경우
        if(count != 0 && allCount != 0) {
            throw new IllegalArgumentException("친구 요청을 이미 보낸 회원입니다.");
        }

        Friend friend = new Friend(requestDto.getToUserId(), requestDto.getStatus(), fromUser);
        Friend saveFriends = friendRepository.save(friend);
        FriendResponseDto friendResponseDto = new FriendResponseDto(saveFriends);
        return friendResponseDto;
    }

    public List<FriendResponseDto> getFriends(User toUser) {
        // 요청 조회시 - 로그인 id == to_user_id
        String status = "PENDING"; // 대기중 상태값의 친구요청만 조회
        return friendRepository.findByToUserIdOrFromUserIdAndStatus(toUser.getId(), toUser.getId(), status).stream().map(FriendResponseDto::new).toList();
    }

    @Transactional
    public Long updateFriend(Long friendId, FriendRequestDto requestDto, User toUser) {

        Friend friend = findFriend(friendId);
        String status = requestDto.getStatus();
        // 요청 응답시 - 로그인 id == to_user_id
        int checkId = friendRepository.findByToUserIdAndStatusAndId(toUser.getId(), status, friendId);
        if(checkId == 0){
            throw new IllegalArgumentException("본인이 받은 요청만 응답할 수 있습니다.");
        }
        if(status.equals("ACCEPT") || status.equals("REJECT")){
            String statusCheck = friendRepository.findAllByStatus(friendId);
            if(statusCheck.equals("PENDING")){
                friend.update(status);
            }else{
                throw new IllegalArgumentException("대기 값만 응답할 수 있습니다.");
            }
        }else{
            throw new IllegalArgumentException("유효하지 않은 상태값입니다. 다시 입력바랍니다.");
        }
        return friendId;
    }

    private Friend findFriend(Long friendId) {
        return friendRepository.findById(friendId).orElseThrow(() ->
                new IllegalArgumentException("비정상적인 접근입니다.")
        );
    }
}
