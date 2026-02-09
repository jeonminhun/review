package com.project.review.domain.user.jwt;

import com.project.review.domain.user.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    private TokenProvider tokenProvider;

    // 1. Secret Key를 Base64 형식의 충분히 긴 문자열로 수정 (HS512 대응)
    private final String TEST_SECRET = "V2hhdGV2ZXJZb3VXYW50VG9Xcml0ZUh0cmVGb3JKV1RTZWNyZXRLZXlUZXN0aW5nQW5kSWRlYWxseUl0U2hvdWxkQmVPdmVyNjRCeXRlc0ZvckhTNTEy";
    private Authentication mockAuthentication;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        // 2. 익명 클래스 사용을 중단하고 실제 객체를 생성하여 private access 에러 해결
        tokenProvider = new TokenProvider(TEST_SECRET);

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        mockAuthentication = new UsernamePasswordAuthenticationToken("testUser", "password", authorities);
    }

    @Test
    @DisplayName("Access 토큰과 Refresh 토큰이 정상적으로 생성되어야 한다")
    void generateTokenDtoTest() {
        TokenDto tokenDto = tokenProvider.generateTokenDto(mockAuthentication);

        assertThat(tokenDto).isNotNull();
        assertThat(tokenDto.getAccessToken()).isNotEmpty();
        assertThat(tokenDto.getRefreshToken()).isNotEmpty();
        log.info("generateTokenDtoTest 확인 : tokenDto = {}", tokenDto);
    }

    @Test
    @DisplayName("생성된 토큰의 정보를 복호화하여 권한을 확인할 수 있어야 한다")
    void getAuthenticationTest() {
        TokenDto tokenDto = tokenProvider.generateTokenDto(mockAuthentication);

        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());

        assertThat(authentication.getName()).isEqualTo("testUser");
        assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("유효한 토큰 검증")
    void validateToken_Valid() {
        TokenDto tokenDto = tokenProvider.generateTokenDto(mockAuthentication);
        boolean isValid = tokenProvider.validateToken(tokenDto.getAccessToken());
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 검증 시 false 반환")
    void validateToken_Expired() {
        // 3. 헬퍼 메서드를 통해 만료된 토큰 생성
        String expiredToken = createExpiredToken(TEST_SECRET);
        boolean isValid = tokenProvider.validateToken(expiredToken);
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("ROLE_ADMIN 권한 토큰은 true를 반환해야 한다")
    void authenticationCheck_IsAdmin() {
        // Given
        Collection<? extends GrantedAuthority> adminAuthorities =
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication adminAuth = new UsernamePasswordAuthenticationToken("adminUser", "password", adminAuthorities);
        TokenDto adminTokenDto = tokenProvider.generateTokenDto(adminAuth);

        // 1. "Bearer " 뒤에 반드시 공백 한 칸을 추가하세요.
        // 2. WebUtils.getCookie()가 내부에서 request.getCookies()를 호출한다면 아래 방식이 더 정확합니다.
        Cookie adminCookie = new Cookie("Authorization", "Bearer " + adminTokenDto.getAccessToken());

        // 이전에 발생했던 WrongTypeOfReturnValue 에러를 방지하기 위해 배열로 전달합니다.
        given(mockRequest.getCookies()).willReturn(new Cookie[]{adminCookie});

        // When
        boolean isAdmin = tokenProvider.AuthenticationCheck(mockRequest);

        // Then
        assertThat(isAdmin).isTrue();
    }

    // 만료된 토큰 생성을 위한 독립적인 헬퍼 메서드
    private String createExpiredToken(String secret) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        Date now = new Date();
        return Jwts.builder()
                .setSubject("expiredUser")
                .claim("auth", "ROLE_USER")
                .setExpiration(new Date(now.getTime() - 1000 * 60)) // 1분 전 만료
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}