package com.project.review.config;

import com.project.review.interceptors.jwtInterceptor;
import com.project.review.domain.user.controller.userController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final userController userController;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new jwtInterceptor(userController))
                .addPathPatterns("/**") // 모든 요청에 대해 인터셉터를 적용
                .excludePathPatterns("/login", "/Register","/assets/**", "/imgs/**","/error"); // 예외 적용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imgs/**")
                .addResourceLocations("file:/app/assets/images/") // 실제 경로
                .setCachePeriod(0); // 캐시 무효화
    }
}
