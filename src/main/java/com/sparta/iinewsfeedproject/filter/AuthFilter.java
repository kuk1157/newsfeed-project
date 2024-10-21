package com.sparta.iinewsfeedproject.filter;

import com.sparta.iinewsfeedproject.entity.User;
import com.sparta.iinewsfeedproject.jwt.JwtUtil;
import com.sparta.iinewsfeedproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@Order(2)
public class AuthFilter implements Filter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletResponse httpServletResponse;

    public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil, HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(url) &&
                (url.startsWith("/api/users/login") || url.startsWith("/api/users/signup"))
        ) {
            chain.doFilter(request, response);
        } else {
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) {
                String token = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(token)) {
                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    httpServletResponse.getWriter().write("유효하지 않은 토큰입니다");
                    return;
                }

                Claims info = jwtUtil.getUserInfoFromToken(token);

                User user = (User)userRepository.findByEmail(info.getSubject()).orElseThrow(() ->
                        new NullPointerException("해당 유저를 찾을 수 없습니다")
                );

                request.setAttribute("user", user);
                chain.doFilter(request, response);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpServletResponse.getWriter().write("토큰을 찾을 수 없습니다");
            }
        }
    }
}
