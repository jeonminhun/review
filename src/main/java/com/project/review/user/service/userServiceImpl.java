package com.project.review.user.service;

import com.project.review.user.dto.*;
import com.project.review.user.entity.UserImg;
import com.project.review.user.jwt.TokenProvider;
import com.project.review.user.Helper;
import com.project.review.user.entity.RefreshToken;
import com.project.review.user.entity.User;
import com.project.review.user.repository.RefreshTokenRepository;
import com.project.review.user.repository.userImgRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class userServiceImpl implements userService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final userRepository userRepository;
    private final userImgRepository userImgRepository;
    private final PasswordEncoder encoder;


    public boolean test(MemberRequestDto memberRequestDto) {
        userRepository.findByuserEmail(memberRequestDto.getUser_email());
        return true;
    }

    @Override
    public User userInfo(Long user_id, HttpServletRequest request) {
        log.info("유저 정보 제공 서비스");
        if (Self_identification(request, user_id)) {
            User user = userRepository.findById(user_id).get();
            return user;
        } else {
            log.info("유저 본인인증 실패");
            return null;
        }


    }

    @Override
    public Long getUserId(HttpServletRequest request) {
        String user_email = tokenProvider.getUserIdFromToken(request);
        User user = userRepository.findByuserEmail(user_email).get();
        return user.getUser_id();
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
                .user_phoneNumber(userCreateDto.getUser_phoneNumber())
                .user_nickName(userCreateDto.getUser_name())
                .user_password(password)
                .userRole(1)
                .build();
        userRepository.save(user);
        return true;
    }

    // 유저 이미지 업데이트 하기 플러스 유저 업데이트
    @Override
    public boolean userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request) {
        log.info("유저 업데이트 시작 : " + userUpdateDto.getUser_id());
        User user = User.builder()
                .user_id(userUpdateDto.getUser_id())
                .user_info(userUpdateDto.getUser_info())
                .user_nickName(userUpdateDto.getUser_nickName())
                .user_phoneNumber(userUpdateDto.getUser_phoneNumber())
                .user_name(userUpdateDto.getUser_name())
                .build();

        if (Self_identification(request, user.getUser_id())) {
            userRepository.UserUpdate(user.getUser_id(), user.getUser_name(), user.getUser_info(), user.getUser_phoneNumber(), user.getUser_nickName());
            return true;
        } else {
            return false;
        }


    }

    @Override
    public boolean imgUpdate(MultipartFile multipartFile, Long user_id, HttpServletRequest request) {

        User user = userRepository.findById(user_id).get();

        log.info("유저 이미지 업데이트 시작 : " + user_id);

        if (Self_identification(request, user.getUser_id())) {
            UserImg userImg = imgSave(multipartFile, user);
            try {
                UserImg userImg_check = userImgRepository.findByUser_id(user_id);
                userImg = UserImg
                        .builder()
                        .user_img_id(userImg_check.getUser_img_id())
                        .user(userImg.getUser())
                        .user_img_name(userImg.getUser_img_name())
                        .build();
            } catch (NullPointerException e) {
            }finally {
                userImgRepository.save(userImg);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean imgDelete(Long user_id, HttpServletRequest request) {

        log.info("유저 이미지 삭제 시작 : " + user_id);

        if (Self_identification(request, user_id)) {
            UserImg userImg = userImgRepository.findByUser_id(user_id);
            imgDelete(userImg);
            userImgRepository.delete(userImg);
            log.info("유저 이미지 삭제 성공");
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto) {
        log.info("로그인 시작");
        memberRequestDto.setUser_password(memberRequestDto.getUser_email() + memberRequestDto.getUser_password());
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

    private boolean Self_identification(HttpServletRequest request, Long user_id) {
        User user = userRepository.findById(user_id).get();
        // 권한 체크 해서 관리자 일경우 그냥 허용하기

        if (tokenProvider.AuthenticationCheck(request)) {
            return true;
        } else if (user.getUserEmail().equals(tokenProvider.getUserIdFromToken(request))) {
            return true;
        } else {
            return false;
        }
    }

    private void imgDelete(UserImg userImg) {
        try {
            Path uploadPath = Path.of("src", "main", "resources", "static", "imgs", "user");
            Path filepath = uploadPath.resolve(userImg.getUser_img_name());
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
        }
    }

    private UserImg imgSave(MultipartFile files, User user) {
        try {
            if (files != null) {
                Path uploadPath = Path.of("src", "main", "resources", "static", "imgs", "user");


                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

//                String fileExtension = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));

                String filename = user.getUser_name() + "_userImg" + ".jpg";
                log.info("유저 이미지 파일 네임 메소드 : " + filename);

                Path filepath = uploadPath.resolve(filename);
                Files.copy(files.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING /*같은 파일이름 있으면 덮어쓰기*/);

                UserImg userImg = UserImg.builder()
                        .user(user)
                        .user_img_name(filename)
                        .build();

                return userImg;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info("이미지 저장 오류");
            e.printStackTrace();
            return null;
        }
    }

}
