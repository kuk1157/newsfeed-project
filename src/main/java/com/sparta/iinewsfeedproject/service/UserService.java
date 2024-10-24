package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.config.PasswordEncoder;
import com.sparta.iinewsfeedproject.dto.*;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.CustomException;
import com.sparta.iinewsfeedproject.exception.ErrorCode;
import com.sparta.iinewsfeedproject.jwt.JwtUtil;
import com.sparta.iinewsfeedproject.repository.FriendRepository;
import com.sparta.iinewsfeedproject.repository.PostRepository;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;

    public UserResponseDto signUp(SignupRequestDto reqDto) {
        userRepository.findByEmail(reqDto.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.DUPLICATION_EMAIL);
                });

        String password = passwordEncoder.encode(reqDto.getPassword());
        User user = new User(reqDto.getName(), reqDto.getEmail());
        user.savePassword(password);

        userRepository.save(user);

        return new UserResponseDto(user);
    }

    public UserResponseDto login(LoginRequestDto reqDto, HttpServletResponse res) {

        User user = (User) userRepository.findByEmail(reqDto.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_MATCH_LOGIN)
        );

        if (!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_LOGIN);
        }

        if (user.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        String token = jwtUtil.createToken(reqDto.getEmail());
        jwtUtil.addJwtToCookie(token, res);

        return new UserResponseDto(user);
    }

    public UserResponseDto showUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        return new UserResponseDto(user);
    }

    @Transactional
    public UserUpdateResponseDto updateName(String name, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        user.setName(name);
        userRepository.save(user);

        return new UserUpdateResponseDto(user);
    }

    @Transactional
    public Object updatePassword(PasswordRequestDto passwordDto, HttpServletRequest httpReq) {
        User user = (User) httpReq.getAttribute("user");

        String pastPassword = passwordDto.getPastPassword();
        String newPassword = passwordDto.getNewPassword();

        // null 체크 추가
        if (pastPassword == null || newPassword == null) {
            throw new CustomException(ErrorCode.NULL_PASSWORD);
        }

        if (pastPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.DUPLICATION_MODIFY_PASSWORD);
        }

        if (!passwordEncoder.matches(pastPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        String password = passwordEncoder.encode(newPassword);
        user.savePassword(password);
        userRepository.save(user);

        return new UserUpdateResponseDto(user);
    }

    @Transactional
    public void deactivateUser(Long userId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        User user = userOptional.get();

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        // 연관된 친구 관계 삭제
        friendRepository.deleteByFromUserId(userId);
        friendRepository.deleteByToUserId(userId);

        // 연관된 게시물 삭제
        postRepository.deleteByUserId(userId);

        // 사용자 비활성화 처리
        user.deactivate();
        userRepository.save(user);
    }
}
