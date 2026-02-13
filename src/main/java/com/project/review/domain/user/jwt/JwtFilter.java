package com.project.review.domain.user.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String BEARER_PREFIX = "Bearer "; // 기존 CSR 방식 일떄  그래서 bearerToken.substring(7); 로 리턴
    public static final String BEARER_PREFIX = "Bearer"; // 쿠키 방식에 맞춰서 변경

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {


        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request, response);

        log.info("토큰확인 : "+ jwt);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        /*헤더에 값이 없다면 토큰 확인*/
        if (bearerToken == null) {
            Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
            if (cookies != null) {
                for (Cookie c : cookies) {
                    log.info("들어온 쿠키 이름: {}, 값: {}", c.getName(), c.getValue());
                    String name = c.getName(); // 쿠키 이름 가져오기

                    String value = c.getValue(); // 쿠키 값 가져오기
                    if (name.equals("Authorization")) {
                        bearerToken = value;
                    }
                }
            }
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {

//            return bearerToken.substring(7);
            return bearerToken.substring(6);
        }
        return null;
    }
}


