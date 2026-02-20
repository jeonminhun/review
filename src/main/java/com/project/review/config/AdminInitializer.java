package com.project.review.config;

import com.project.review.domain.user.entity.User;
import com.project.review.domain.user.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            // 1. 어드민 계정이 이미 있는지 확인 (이메일 기준)
            if (userRepository.findByuserEmail("admin@test.com").isEmpty()) {
                // 2. 없으면 새로 생성
                User admin = User.builder()
                        .userEmail("admin@test.com")
                        .user_name("관리자")
                        .user_password(passwordEncoder.encode("admin@test.com"+"admin1234")) // 비밀번호 암호화 필수!
                        .user_phoneNumber("010-1111-2222")
                        .user_nickName("관리자")
                        .userRole(9)
                        .build();

                userRepository.save(admin);
                System.out.println("어드민 계정이 생성되었습니다: admin@test.com");
            }
        };
    }
}