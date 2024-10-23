package com.sparta.iinewsfeedproject.controller;


import com.sparta.iinewsfeedproject.dto.FriendRequestDto;
import com.sparta.iinewsfeedproject.dto.FriendResponseDto;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<FriendResponseDto> createFriend(@RequestBody FriendRequestDto requestDto, HttpServletRequest request) {
        User fromUser = getUser(request);
        FriendResponseDto friend = friendService.createFriend(requestDto, fromUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(friend);
    }

    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> getFriends(HttpServletRequest request) {
        User toUser = getUser(request);
        List<FriendResponseDto> responseDto = friendService.getFriends(toUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{friendId}")
    public Long updateFriend(@PathVariable Long friendId, @RequestBody FriendRequestDto requestDto, HttpServletRequest request) {
        User toUser = getUser(request);
        return friendService.updateFriend(friendId,requestDto,toUser);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long friendId, HttpServletRequest request) {
        // JWT 토큰에서 추출한 사용자
        User user = (User) request.getAttribute("user");
        friendService.deleteFriend(friendId, user.getId());
        return ResponseEntity.ok().build();
    }

    public User getUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }
}
