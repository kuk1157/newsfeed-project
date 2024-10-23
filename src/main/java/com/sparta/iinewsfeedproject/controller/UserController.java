package com.sparta.iinewsfeedproject.controller;

import com.sparta.iinewsfeedproject.dto.*;
import com.sparta.iinewsfeedproject.exception.IncorrectPasswordException;
import com.sparta.iinewsfeedproject.exception.UserNotFoundException;
import com.sparta.iinewsfeedproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto reqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            ErrorResponseDto errorResponse = new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    errorMessage
            );
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
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
    public ResponseEntity<UserResponseDto> showUser(@PathVariable Long userid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.showUser(userid));
    }

    @PutMapping("/name")
    public ResponseEntity<UserUpdateResponseDto> updateName(@RequestBody Map<String, String> request, HttpServletRequest httpReq) {
        String name = request.get("name");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateName(name, httpReq));
    }

    @PutMapping("/password")
    public ResponseEntity<UserUpdateResponseDto> updatePassword(@RequestBody PasswordRequestDto passwordDto, HttpServletRequest HttpReq) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body((UserUpdateResponseDto) userService.updatePassword(passwordDto, HttpReq));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Map<String, List<FriendDto>>> getFriends(@PathVariable Long userId) {
        List<FriendDto> friends = friendService.getAcceptedFriends(userId);
        Map<String, List<FriendDto>> response = new HashMap<>();
        response.put("contents", friends);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId, @RequestBody DeleteUserRequestDto deleteUserRequest) {
        userService.deactivateUser(userId, deleteUserRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(404, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleIncorrectPasswordException(IncorrectPasswordException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
