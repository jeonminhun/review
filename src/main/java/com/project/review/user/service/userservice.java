package com.project.review.user.service;

import com.project.review.user.dto.*;
import com.project.review.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface userService {

    User userInfo(Long user_id, HttpServletRequest request);

    Long getUserId(HttpServletRequest request);

    boolean checkPassWord(userCreateDto userCreateDto);

    boolean createUser(userCreateDto userCreateDto);

    boolean userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request);

    boolean imgUpdate(MultipartFile multipartFile, Long user_id, HttpServletRequest request);

    TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto);

    TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request);

}
