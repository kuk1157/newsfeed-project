package com.sparta.iinewsfeedproject.controller;

import com.sparta.iinewsfeedproject.dto.*;
import com.sparta.iinewsfeedproject.exception.UserNotFoundException;
import com.sparta.iinewsfeedproject.service.FriendService;
import com.sparta.iinewsfeedproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    @Autowired
    private FriendService friendService;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody SignupRequestDto reqDto) {
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signUp(reqDto));
    }

    @PutMapping("/name")
    public ResponseEntity<UserResponseDto> updateName(@RequestBody String name, HttpServletRequest httpReq) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateName(name, httpReq));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody LoginRequestDto reqDto, HttpServletResponse res) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(reqDto, res));
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponseDto> showUser(@PathVariable Long userid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.showUser(userid));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Map<String, List<FriendDto>>> getFriends(@PathVariable Long userId) {
        List<FriendDto> friends = friendService.getAcceptedFriends(userId);
        Map<String, List<FriendDto>> response = new HashMap<>();
        response.put("contents", friends);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
