package com.project.review.user.service;


import com.project.review.user.entity.User;

import com.project.review.user.repository.userRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final userRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("로그 검증 시작"+email);
        Optional<User> user =  userRepository.findByuserEmail(email);
        log.info("로그 검증 시작 쿼리 진행 후"+user);
        return
                user.map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(user.getUserRole()));
        log.info("로그 검증 = "+user.getUserEmail());
        return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getUser_password(),
                Collections.singleton(grantedAuthority)
        );
    }
}