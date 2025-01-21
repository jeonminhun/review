package com.project.review.user.service;

import com.project.review.user.jwt.TokenProvider;
import com.project.review.user.Helper;
import com.project.review.user.dto.MemberRequestDto;
import com.project.review.user.dto.TokenDto;
import com.project.review.user.dto.TokenRequestDto;
import com.project.review.user.dto.userCreateDto;
import com.project.review.user.entity.RefreshToken;
import com.project.review.user.entity.User;
import com.project.review.user.repository.RefreshTokenRepository;
import com.project.review.user.repository.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class userServiceImpl implements userService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final userRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public boolean test(MemberRequestDto memberRequestDto) {
        userRepository.findByuserEmail(memberRequestDto.getUser_email());
        return true;
    }

    @Override
    public boolean checkPassWord(userCreateDto userCreateDto) {
        return userCreateDto.getUser_password().equals(userCreateDto.getUser_password_check());
    }

    @Override
    public boolean createUser(userCreateDto userCreateDto) {
        String password = encoder.encode(userCreateDto.getUser_email() + userCreateDto.getUser_password());
        User user = User.builder()
                .userEmail(userCreateDto.getUser_email())
                .user_name(userCreateDto.getUser_name())
                .user_password(password)
                .userRole(1)
                .build();
        userRepository.save(user);
        return true;
    }
    @Override
    @Transactional
    public TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto) {
        log.info("로그인 시작");
        memberRequestDto.setUser_password(memberRequestDto.getUser_email()+memberRequestDto.getUser_password());
//        memberRequestDto.setPassword(memberRequestDto.getPassword());
        log.info("로그인 setPassword 성공");
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();
        log.info("토큰 생성 성공");
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("검증 성공");
        Optional<User> user = userRepository.findByuserEmail(memberRequestDto.getUser_email());
        log.info("repository 데이터 검증 성공");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        log.info("jwt 토큰 생성 성공");
        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .ip(Helper.getClientIp(request))
                .build();
        refreshTokenRepository.save(refreshToken);
        log.info("refresh 토큰 저장 성공");

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request) {
        System.out.println("이거맞아? : " + tokenRequestDto.getRefreshToken());

        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다."); //이부분체크 필요
        }
        String currentIpAddress = Helper.getClientIp(request);
        if (refreshToken.getIp().equals(currentIpAddress)) {

            Optional<User> user = userRepository.findByuserEmail(authentication.getName());

            // 5. 새로운 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // 6. 저장소 정보 업데이트
            RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
            refreshTokenRepository.save(newRefreshToken);

            // 토큰 발급
            return tokenDto;
        } else {
            throw new RuntimeException("ip가 일치하지 않습니다.");
        }
    }

}
