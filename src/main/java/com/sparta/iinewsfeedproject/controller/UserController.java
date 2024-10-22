package com.sparta.iinewsfeedproject.controller;

import com.sparta.iinewsfeedproject.dto.LoginRequestDto;
import com.sparta.iinewsfeedproject.dto.SignupRequestDto;
import com.sparta.iinewsfeedproject.dto.UserResponseDto;
import com.sparta.iinewsfeedproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody SignupRequestDto reqDto) {
        return  ResponseEntity
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
    public ResponseEntity<UserResponseDto> showUser(@PathVariable Long userid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.showUser(userid));
    }

}
