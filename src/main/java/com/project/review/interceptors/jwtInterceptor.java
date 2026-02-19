package com.project.review.interceptors;

import com.project.review.domain.user.controller.userController;
import com.project.review.domain.user.dto.TokenRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import java.util.Date;

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
                    try {// reissue 호출 부분을 try-catch로 감싸서 인터셉터 때문에 앱이 죽지 않게 합니다.
                        log.info("accessToken 갱신 :{}", cookies[0].getValue());

                        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
                        Cookie RefCookie = WebUtils.getCookie(request, "RefreshToken");
                        tokenRequestDto.setAccessToken(authCookie.getValue().substring(6));
                        tokenRequestDto.setRefreshToken(RefCookie.getValue().substring(6));
                        userController.reissue(tokenRequestDto, request, response);
                    }catch (Exception e){
                        // 여기서 에러가 나도 throw 하지 않고 로그만 찍습니다.
                        // 그래야 다음 단계(이미지 핸들러나 에러 핸들러)로 넘어갈 수 있습니다.
                        log.error("인터셉터 토큰 갱신 중 에러 발생(무시하고 진행): {}", e.getMessage());
                    }


                }
            }
        }

        long endTime = System.currentTimeMillis(); // 실행 종료 시간 측정
        log.info("jwtInterceptor 실행 시간: {} ms", (endTime - startTime));
        return true;
    }
}
