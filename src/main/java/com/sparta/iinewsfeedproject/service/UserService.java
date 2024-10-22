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

    public UserResponseDto showUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new NullPointerException("해당 유저는 찾을 수 없습니다")
        );

        return new UserResponseDto(user);
    }

}
