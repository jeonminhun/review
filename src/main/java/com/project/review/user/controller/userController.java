package com.project.review.user.controller;

import com.project.review.user.dto.*;
import com.project.review.user.service.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

@Controller
@RequiredArgsConstructor
@Slf4j
public class userController {

    private final userService userService;

    @GetMapping("/Register")
    public String create_form(Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            return "after/index";
        }

        userCreateDto userCreateDto = new userCreateDto();
        model.addAttribute("userCreateDto", userCreateDto);
        return "default/Register";

    }

    @PostMapping("/Register")
    public String create_form(@ModelAttribute userCreateDto userCreateDto, BindingResult bindingResult, Model model) {
        if (!userService.checkPassWord(userCreateDto)) {
            bindingResult.addError(new FieldError("userCreateDto", "user_password", "비밀번호가 다릅니다."));
            return "default/Register";
        }
        userService.createUser(userCreateDto);
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String login_form(Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            return "after/index";
        }
        log.info("로그인 페이지 로그");
        return "default/login";


    }

    @PostMapping("/login")
    public String login_form(@ModelAttribute MemberRequestDto memberRequestDto, HttpServletRequest request, HttpServletResponse response, Model model) {

        log.info("로그인 시도 전 로그" + memberRequestDto.getUser_email());
        TokenDto accessToken = userService.login(request, memberRequestDto);
        setCookie(response, accessToken);

        return "redirect:/";

    }

    // 프로필 사진 기능 업데이트 : 프로필삭제, 프로필 수정, 프로필 추가()
    @PostMapping("/userSet")
    public String user_update(
            @ModelAttribute UserUpdateDto userUpdateDto,
            HttpServletRequest request,
            Model model) {

        log.info("유저 업데이트 전 로그" + userUpdateDto.getUser_name());

        if (userService.userUpdate(userUpdateDto, request)) {
            return "redirect:/myData/"+userUpdateDto.getUser_id();
        } else {
            return "redirect:/";
        }


    }

    @PostMapping("/imgSet/{user_id}")
    public String user_update(
            @RequestPart("file") MultipartFile multipartFile,
            @PathVariable("user_id") Long user_id,
            HttpServletRequest request,
            Model model) {

        log.info("유저 이미지 업데이트 로그");

        if (userService.imgUpdate(multipartFile, user_id, request)) {
            return "redirect:/myData/"+user_id;
        } else {
            return "redirect:/";
        }
    }

    @ResponseBody
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<String> user_Delete(
            @PathVariable("user_id") Long user_id,
            HttpServletRequest request) {

        log.info("유저 이미지 업데이트 로그");

        if (userService.imgDelete(user_id, request)) {
            return ResponseEntity.ok("이미지 삭제 성공");
        } else {
            log.error("유저 이미지 삭제 실패, userId: {}", user_id);
            return ResponseEntity.internalServerError().body("이미지 삭제 실패");
        }
    }

    public void reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request, HttpServletResponse response) {
        TokenDto accessToken = userService.reissue(tokenRequestDto, request);
        setCookie(response, accessToken);
    }


    private static void setCookie(HttpServletResponse response, TokenDto accessToken) {
        Cookie accessCookie = new Cookie("Authorization", accessToken.getGrantType() + accessToken.getAccessToken());
        accessCookie.setMaxAge(60 * 30); // 30분 동안 유효
        accessCookie.setPath("/");
        accessCookie.setDomain("localhost");
        accessCookie.setSecure(false);
        response.addCookie(accessCookie);

        Cookie accesstimeCookie = new Cookie("time", accessToken.getAccessTokenExpiresIn().toString());
        accesstimeCookie.setMaxAge(60 * 30); // 30분 동안 유효
        accesstimeCookie.setPath("/");
        accesstimeCookie.setDomain("localhost");
        accesstimeCookie.setSecure(false);
        response.addCookie(accesstimeCookie);

        Cookie refreshCookie = new Cookie("RefreshToken", accessToken.getGrantType() + accessToken.getRefreshToken());
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유효
        refreshCookie.setPath("/");
        refreshCookie.setDomain("localhost");
        refreshCookie.setSecure(false);

        response.addCookie(refreshCookie);
    }
}
