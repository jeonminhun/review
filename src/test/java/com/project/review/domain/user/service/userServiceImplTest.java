package com.project.review.domain.user.service;

import com.project.review.domain.user.Helper;
import com.project.review.domain.user.dto.*;
import com.project.review.domain.user.entity.RefreshToken;
import com.project.review.domain.user.entity.User;
import com.project.review.domain.user.entity.UserImg;
import com.project.review.domain.user.jwt.TokenProvider;
import com.project.review.domain.user.redisRepository.RefreshTokenRepository;
import com.project.review.domain.user.repository.userImgRepository;
import com.project.review.domain.user.repository.userRepository;
import com.project.review.global.util.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@Slf4j
@ExtendWith(MockitoExtension.class)
class userServiceImplTest {

    @InjectMocks
    private userServiceImpl userService;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private userRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private userImgRepository userImgRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private FileService fileService;
    @Mock
    private Helper helper;


    @Test
    void userInfoTest() {
        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();


        given(userRepository.findById(1L)).willReturn(Optional.of(testuser));
        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);

        User result = userService.userInfo(1L, request);

        assertNotNull(result);
        assertEquals("test@test.com", result.getUserEmail());
        assertEquals("홍길동", result.getUser_name());
    }

    @Test
    void getUserIdTest() {
        String testEmail = "test@test.com";
        User testuser = User.builder()
                .user_id(1L).userEmail(testEmail).user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        given(tokenProvider.getUserIdFromToken(request)).willReturn(testEmail);
        given(userRepository.findByuserEmail(testEmail)).willReturn(Optional.of(testuser));

        Long userId = userService.getUserId(request);

        assertThat(userId).isEqualTo(1L);
        assertThrows(NoSuchElementException.class, () -> {
            userService.getUserId(null);
        });

        verify(tokenProvider).getUserIdFromToken(request);
        verify(userRepository).findByuserEmail(testEmail);
    }

    @Test
    void findAllUserTest() {
        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();
        User testuser2 = User.builder()
                .user_id(2L).userEmail("test2@test.com").user_password("asdf2").user_name("홍길동2").user_phoneNumber("010-1234-1234").build();
        User testuser3 = User.builder()
                .user_id(3L).userEmail("test@test.com3").user_password("asdf3").user_name("홍길동3").user_phoneNumber("010-1234-1234").build();
        User testuser4 = User.builder()
                .user_id(4L).userEmail("test@test.com4").user_password("asdf4").user_name("홍길동4").user_phoneNumber("010-1234-1234").build();

        List<User> userList = new ArrayList<>();
        userList.add(testuser);
        userList.add(testuser2);
        userList.add(testuser3);
        userList.add(testuser4);

        given(userRepository.findAll()).willReturn(userList);

        List<User> allUser = userService.findAllUser();

        assertThat(allUser).hasSize(4);
        assertThat(allUser).contains(testuser, testuser4);
    }

    @Test
    void createUserTest() {
        userCreateDto userCreateDto = new userCreateDto();
        userCreateDto.setUser_password("asdf");
        userCreateDto.setUser_email("test@test.com");
        userCreateDto.setUser_name("test");
        userCreateDto.setUser_phoneNumber("010-1234-1234");

        given(encoder.encode(anyString())).willReturn("asdf");

        boolean user = userService.createUser(userCreateDto);

        assertThat(user).isTrue();

        // 실행 데이터 캡쳐 도구 ArgumentCaptor
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        // 실행 검증함수 verify,
        verify(userRepository, times(1)).save(captor.capture());

        User result = captor.getValue();
        assertThat(result.getUser_phoneNumber()).isEqualTo("010-1234-1234");
        assertThat(result.getUser_name()).isEqualTo("test");
        assertThat(result.getUser_password()).isEqualTo("asdf");
        assertThat(result.getUserEmail()).isEqualTo("test@test.com");


    }

    @Test
    void userUpdateTest() {

        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setUser_id(1L);
        userUpdateDto.setUser_name("asdf");
        userUpdateDto.setUser_phoneNumber("010-1234-1234");
        userUpdateDto.setUser_nickName("test");
        userUpdateDto.setUser_info("testtesttest");

//        given(encoder.encode(anyString())).willReturn("asdf");\

        given(userRepository.findById(1L)).willReturn(Optional.of(testuser));
        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);


        boolean user = userService.userUpdate(userUpdateDto, request);
//        given();

        assertThat(user).isTrue();


        verify(userRepository, times(1)).UserUpdate(
                eq(userUpdateDto.getUser_id()),
                eq(userUpdateDto.getUser_name()),
                eq(userUpdateDto.getUser_info()),
                eq(userUpdateDto.getUser_phoneNumber()),
                eq(userUpdateDto.getUser_nickName())
        );


    }

    @Test
    void imgUpdateTest() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());

        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        UserImg testUserImg = UserImg.builder().user_img_id(10L).user_img_name("test.img").user(testuser).build();

        given(userRepository.findById(1L)).willReturn(Optional.of(testuser));

        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);

        given(userImgRepository.findByUser_id(1L)).willReturn(testUserImg);

        given(fileService.imgSave(any(), anyString(), anyString())).willReturn("1_userImg.jpg");

        boolean result = userService.imgUpdate(file, 1L, request);

        assertThat(result).isTrue();

        ArgumentCaptor<UserImg> captor = ArgumentCaptor.forClass(UserImg.class);

        verify(userImgRepository, times(1)).save(captor.capture());
        verify(fileService, times(1)).imgSave(eq(file), eq("user"), eq("1_userImg.jpg"));

        UserImg savedImg = captor.getValue();
        assertThat(savedImg.getUser_img_id()).isEqualTo(10L); // ID가 유지되었는지 확인
        assertThat(savedImg.getUser_img_name()).isEqualTo("1_userImg.jpg"); // 파일 이름이 바뀌었는지 확인 (imgSave 로직에 따라 다름)


    }

    @Test
    void ImgDeleteTest() {

        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        UserImg testUserImg = UserImg.builder().user_img_id(10L).user_img_name("test.img").user(testuser).build();

        given(userImgRepository.findByUser_id(1L)).willReturn(testUserImg);
        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);
        given(userRepository.findById(1L)).willReturn(Optional.of(testuser));


        boolean result = userService.imgDelete(1L, request);

        assertThat(result).isTrue();

        ArgumentCaptor<UserImg> captor = ArgumentCaptor.forClass(UserImg.class);

        verify(userImgRepository, times(1)).delete(captor.capture());

        verify(fileService, times(1)).imgDelete(eq("user"), eq("test.img"));

        UserImg value = captor.getValue();
        assertThat(value.getUser_img_id()).isEqualTo(10L);
        assertThat(value.getUser_img_name()).isEqualTo("test.img");
    }


    @Test
    void loginTest() {

        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setUser_email("test@test.com");
        memberRequestDto.setUser_password("asdf");

        Authentication authentication = Mockito.mock(Authentication.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

        TokenDto mockTokenDto = TokenDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any())).willReturn(authentication);

        given(authentication.getName()).willReturn("test@test.com");

        given(tokenProvider.generateTokenDto(authentication)).willReturn(mockTokenDto);

        TokenDto login = userService.login(request, memberRequestDto);

        assertThat(login.getAccessToken()).isEqualTo("access-token");
        assertThat(login.getRefreshToken()).isEqualTo("refresh-token");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(1)).save(captor.capture());

        RefreshToken value = captor.getValue();

        assertThat(value.getValue()).isEqualTo("refresh-token");
        assertThat(value.getKey()).isEqualTo("test@test.com");
    }

    @Test
    void reissueTest() {

        TokenRequestDto tokenRequestDto = new  TokenRequestDto();
        tokenRequestDto.setAccessToken("access-token");
        tokenRequestDto.setRefreshToken("refresh-token");
//        tokenRequestDto.setRefreshToken("refresh-token2");

        TokenDto mockTokenDto = TokenDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        RefreshToken mockrefreshToken = RefreshToken.builder().key("test@test.com").value("refresh-token").ip("asdf").build();
//        RefreshToken mockrefreshToken = RefreshToken.builder().key("test@test.com").value("refresh-token").ip("aaaa").build();


        Authentication authentication = Mockito.mock(Authentication.class);

        given(tokenProvider.validateToken(any())).willReturn(true);
//        given(tokenProvider.validateToken(any())).willReturn(false);
        given(tokenProvider.getAuthentication(any())).willReturn(authentication);
        given(authentication.getName()).willReturn("test@test.com");
        given(refreshTokenRepository.findByKey(authentication.getName())).willReturn(Optional.ofNullable(mockrefreshToken));
        given(helper.getClientIp(request)).willReturn("asdf");
        given(userRepository.findByuserEmail("test@test.com")).willReturn(Optional.ofNullable(testuser));
        given(tokenProvider.generateTokenDto(authentication)).willReturn(mockTokenDto);

        TokenDto reissue = userService.reissue(tokenRequestDto, request);

        assertThat(reissue.getAccessToken()).isEqualTo("access-token");
        assertThat(reissue.getRefreshToken()).isEqualTo("refresh-token");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository, times(1)).save(captor.capture());

        RefreshToken value = captor.getValue();
        assertThat(value.getValue()).isEqualTo("refresh-token");
        assertThat(value.getKey()).isEqualTo(authentication.getName());
    }

}
