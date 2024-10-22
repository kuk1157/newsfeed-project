package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.config.PasswordEncoder;
import com.sparta.iinewsfeedproject.dto.LoginRequestDto;
import com.sparta.iinewsfeedproject.dto.SignupRequestDto;
import com.sparta.iinewsfeedproject.dto.UserResponseDto;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.exception.IncorrectPasswordException;
import com.sparta.iinewsfeedproject.exception.UserNotFoundException;
import com.sparta.iinewsfeedproject.jwt.JwtUtil;
import com.sparta.iinewsfeedproject.repository.FriendRepository;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    private FriendRepository friendRepository;

    public UserResponseDto signUp(SignupRequestDto reqDto){
        userRepository.findByEmail(reqDto.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("중복된 이메일 입니다");
                });

        String password = passwordEncoder.encode(reqDto.getPassword());
        User user = new User(reqDto.getName(), reqDto.getEmail());
        user.savePassword(password);

        userRepository.save(user);

        return new UserResponseDto(user);
    }

    public UserResponseDto login(LoginRequestDto reqDto, HttpServletResponse res){

        User user = (User) userRepository.findByEmail(reqDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다")
        );

        if(!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다");
        }

        if(user.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 유저는 찾을 수 없습니다");
        }

        String token = jwtUtil.createToken(reqDto.getEmail());
        jwtUtil.addJwtToCookie(token, res);

        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 유저번호 입니다.");
        }

        User user = userOptional.get();

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("비밀번호가 일치하지 않습니다.");
        }

        // 연관된 친구 관계 삭제
        friendRepository.deleteByFromUserId(userId);
        friendRepository.deleteByToUserId(userId);

        // 사용자 삭제
        userRepository.deleteById(userId);
    }

    public UserResponseDto showUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new NullPointerException("해당 유저는 찾을 수 없습니다")
        );

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateName(String name, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");

        user.setName(name);

        return new UserResponseDto(user);
    }
}
