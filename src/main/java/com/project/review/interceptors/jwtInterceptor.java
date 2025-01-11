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
import org.springframework.web.util.WebUtils;

import java.util.Date;
import static com.project.review.user.jwt.JwtFilter.BEARER_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class jwtInterceptor implements HandlerInterceptor {

    private final userController userController;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        TokenRequestDto tokenRequestDto = new TokenRequestDto();

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        if (cookies != null) {
            Cookie authCookieTime = WebUtils.getCookie(request, "time");
            if (authCookieTime != null) {

                String time = authCookieTime.getValue();
                long timestampLong = Long.parseLong(time);

                Date date1 = new Date();
                Date date2 = new Date(timestampLong);

                long diffInMillis = Math.abs(date2.getTime() - date1.getTime());
                diffInMillis = diffInMillis / (60 * 1000);

                log.info("accessToken 유효시간이 {}분 남았습니다.", diffInMillis);

                if (5 >= diffInMillis) { // 토큰 유효시간이 5분미만이면 갱신시작
                    log.info("accessToken 갱신 :{}", cookies[0].getValue());

                    Cookie authCookie = WebUtils.getCookie(request, "Authorization");
                    Cookie RefCookie = WebUtils.getCookie(request, "RefreshToken");
                    tokenRequestDto.setAccessToken(authCookie.getValue().substring(6));
                    tokenRequestDto.setRefreshToken(RefCookie.getValue().substring(6));
                    userController.reissue(tokenRequestDto, request, response);

                }
            }
        }

        long endTime = System.currentTimeMillis(); // 실행 종료 시간 측정
        log.info("jwtInterceptor 실행 시간: {} ms", (endTime - startTime));
        return true;
    }
}
