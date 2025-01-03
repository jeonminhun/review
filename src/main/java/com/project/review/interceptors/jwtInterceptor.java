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
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Date;

import static com.project.review.user.jwt.JwtFilter.AUTHORIZATION_HEADER;
import static com.project.review.user.jwt.JwtFilter.BEARER_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class jwtInterceptor implements HandlerInterceptor {

    private final userController userController;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        if (cookies != null) {

            String time =  cookies[2].getValue();

            long timestampLong = Long.parseLong(time);
            Date date1 = new Date();
            Date date2 = new Date(timestampLong);
            long diffInMillies = Math.abs(date2.getTime()- date1.getTime());
            diffInMillies = diffInMillies / (60 * 1000);
            log.info("accessToken 유효시간이 "+diffInMillies+"분 남았습니다.");

            if (5>=diffInMillies){ // 토큰 유효시간이 5분미만이면 갱신시작
                log.info("accessToken 갱신 :"+ cookies[0].getValue());
                for (Cookie c : cookies) {
                    String name = c.getName(); // 쿠키 이름 가져오기

                    String value = c.getValue(); // 쿠키 값 가져오기
                    if (name.equals("Authorization")) {
                        if (StringUtils.hasText(value) && value.startsWith(BEARER_PREFIX)) {
                            tokenRequestDto.setAccessToken(value.substring(6));
                        }
                    }
                    else if (name.equals("RefreshToken")) {
                        if (StringUtils.hasText(value) && value.startsWith(BEARER_PREFIX)) {
                            tokenRequestDto.setRefreshToken(value.substring(6));
                        }
                    }
                }
                if (tokenRequestDto.getAccessToken()!=null&&tokenRequestDto.getRefreshToken()!=null){
                    userController.reissue(tokenRequestDto,request,response);
                }
            }
        }





        return true;
    }
}
