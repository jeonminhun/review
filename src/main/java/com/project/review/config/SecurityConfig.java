package com.project.review.config;

import com.project.review.user.jwt.JwtAccessDeniedHandler;
import com.project.review.user.jwt.JwtAuthenticationEntryPoint;
import com.project.review.user.jwt.JwtFilter;
import com.project.review.user.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*필터체인 요청 경로 로그*/
    private static class FilterChainLogger extends GenericFilterBean {
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            log.info("필터 체인 동작: 요청 URI = {}","["+httpRequest.getMethod()+"]"+ " : " + httpRequest.getRequestURI());

            chain.doFilter(request, response);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        크로스 설정
       http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));

        //csrf disable
        http.csrf((auth) -> auth.disable())

                .exceptionHandling((auth)->auth.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                //경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/assets/**","/chart-data").permitAll()
                        .requestMatchers("/","/Register","/login","/shop","/search").permitAll()
                        .requestMatchers("/admin").hasRole("ADMINISTRATOR")
                        .anyRequest().authenticated())

                //로그아웃 설정
                .logout((logout)->
                        logout.logoutUrl("/logout")// 로그아웃 요청 URL
                                .logoutSuccessUrl("/")// 로그아웃 후 리다이렉트 경로 설정
                                .deleteCookies("Authorization")// 로그아웃 시 쿠키 삭제
                                .deleteCookies("time")// 로그아웃 시 쿠키 삭제
                                .deleteCookies("RefreshToken")// 로그아웃 시 쿠키 삭제
                                .invalidateHttpSession(true))// 세션 무효화

                // tokenProvider 주입
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new FilterChainLogger(), SecurityContextPersistenceFilter.class);
        return http.build();
    }

}
