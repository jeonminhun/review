package com.project.review.user.service;

import com.project.review.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface userService {

    boolean checkPassWord(userCreateDto userCreateDto);

    boolean createUser(userCreateDto userCreateDto);

    boolean userUpdate(MultipartFile multipartFile, UserUpdateDto userUpdateDto, HttpServletRequest request);

    TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto);

    TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request);

}
