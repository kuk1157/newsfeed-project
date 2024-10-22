package com.sparta.iinewsfeedproject.controller;


import com.sparta.iinewsfeedproject.dto.FriendRequestDto;
import com.sparta.iinewsfeedproject.dto.FriendResponseDto;
import com.sparta.iinewsfeedproject.entity.Friend;
import com.sparta.iinewsfeedproject.dto.ErrorResponseDto;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.FriendNotFoundException;
import com.sparta.iinewsfeedproject.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("")
    public ResponseEntity<FriendResponseDto> createFriend(@RequestBody FriendRequestDto requestDto, HttpServletRequest request) {
        User fromUser = (User)request.getAttribute("fromUser");
        FriendResponseDto friend = friendService.createFriend(requestDto,fromUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(friend);
    }

    @GetMapping("")
    public ResponseEntity<List<FriendResponseDto>> getFriends() {
        List<FriendResponseDto> responseDto = friendService.getFriends();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("")
    public ResponseEntity<List<FriendResponseDto>> getFriends() {
        List<FriendResponseDto> responseDto = friendService.getFriends();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping ("/{fromUserId}/friend/{userId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long fromUserId, @PathVariable Long userId) {
        friendService.deleteFriend(fromUserId, userId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(FriendNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleFriendNotFoundException(FriendNotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
