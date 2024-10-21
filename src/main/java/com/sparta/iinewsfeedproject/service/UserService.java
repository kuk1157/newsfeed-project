package com.sparta.iinewsfeedproject.service;

import com.sparta.iinewsfeedproject.config.PasswordEncoder;
import com.sparta.iinewsfeedproject.dto.LoginRequestDto;
import com.sparta.iinewsfeedproject.dto.SignupRequestDto;
import com.sparta.iinewsfeedproject.dto.UserResponseDto;
import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.jwt.JwtUtil;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UserResponseDto signUp(SignupRequestDto reqDto){
        userRepository.findByEmail(reqDto.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다");
                });

        String password = passwordEncoder.encode(reqDto.getPassword());
        User user = new User(reqDto.getName(), reqDto.getEmail());
        user.savePassword(password);

        userRepository.save(user);

        return new UserResponseDto(user);
    }

}
