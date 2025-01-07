package com.project.review.interceptors;

import com.project.review.user.controller.userController;
import com.project.review.user.dto.TokenRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Date;
import static com.project.review.user.jwt.JwtFilter.BEARER_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class jwtInterceptor implements HandlerInterceptor {
    long startTime = System.currentTimeMillis();

    private final userController userController;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("time")) {
                    String time = cookie.getValue();
                    long timestampLong = Long.parseLong(time);
                    Date date1 = new Date();
                    Date date2 = new Date(timestampLong);
                    long diffInMillis = Math.abs(date2.getTime() - date1.getTime());
                    diffInMillis = diffInMillis / (60 * 1000);
                    log.info("accessToken 유효시간이 {}분 남았습니다.", diffInMillis);
                    Cookie authorizationCookie = null;
                    Cookie refreshTokenCookie = null;
                    if (5 >= diffInMillis) { // 토큰 유효시간이 5분미만이면 갱신시작
                        log.info("accessToken 갱신 :{}", cookies[0].getValue());
                        for (Cookie c : cookies) {
                            if (c.getName().equals("Authorization")) {
                                authorizationCookie = c;
                            } else if (c.getName().equals("RefreshToken")) {
                                refreshTokenCookie = c;
                            }
                        }
                        if (tokenRequestDto.getAccessToken() != null && tokenRequestDto.getRefreshToken() != null) {
                            tokenRequestDto.setAccessToken(authorizationCookie.getValue().substring(6));
                            tokenRequestDto.setRefreshToken(refreshTokenCookie.getValue().substring(6));
                            userController.reissue(tokenRequestDto, request, response);
                        }
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis(); // 실행 종료 시간 측정
        log.info("jwtInterceptor 실행 시간: {} ms", (endTime - startTime));
        return true;
    }
}
