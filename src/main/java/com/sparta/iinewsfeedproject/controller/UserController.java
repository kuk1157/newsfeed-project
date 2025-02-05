package com.sparta.iinewsfeedproject.controller;

import com.sparta.iinewsfeedproject.dto.*;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.service.FriendService;
import com.sparta.iinewsfeedproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto reqDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signUp(reqDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@RequestBody LoginRequestDto reqDto, HttpServletResponse res) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(reqDto, res));
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long userid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(userid));
    }

    @PutMapping("/name")
    public ResponseEntity<UserUpdateResponseDto> updateName(@RequestBody NameRequestDto requestDto, HttpServletRequest httpReq) {
        String name = requestDto.getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateName(name, httpReq));
    }

    @PutMapping("/password")
    public ResponseEntity<UserUpdateResponseDto> updatePassword(@Valid @RequestBody PasswordRequestDto passwordDto, HttpServletRequest HttpReq) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updatePassword(passwordDto, HttpReq));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Map<String, List<FriendDto>>> getFriends(@PathVariable Long userId) {
        List<FriendDto> friends = friendService.getAcceptedFriends(userId);
        Map<String, List<FriendDto>> response = new HashMap<>();
        response.put("contents", friends);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId, @RequestBody DeleteUserRequestDto deleteUserRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        if (!user.getId().equals(userId)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        userService.deactivateUser(userId, deleteUserRequest.getPassword());
        return ResponseEntity.noContent().build();
    }
}
