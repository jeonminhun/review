package com.project.review.config;

import com.project.review.interceptors.jwtInterceptor;
import com.project.review.domain.user.controller.userController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final userController userController;
    // 여기서도 똑같이 설정 값을 읽어옵니다.
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new jwtInterceptor(userController))
                .addPathPatterns("/**") // 모든 요청에 대해 인터셉터를 적용
                .excludePathPatterns("/login", "/Register","/assets/**", "/imgs/**","/error"); // 예외 적용
    }


    // aws에 맞춰 경로 수정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/images/**")
                .addResourceLocations("file:" + uploadDir) // 실제 경로
                .setCachePeriod(0); // 캐시 무효화
    }
}
