package com.project.review.domain.user.service;

import com.project.review.domain.user.dto.*;
import com.project.review.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface userService {

    User userInfo(Long user_id, HttpServletRequest request);

    Long getUserId(HttpServletRequest request);

    List<User> findAllUser();

    boolean checkPassWord(userCreateDto userCreateDto);

    boolean createUser(userCreateDto userCreateDto);

    boolean userUpdate(UserUpdateDto userUpdateDto, HttpServletRequest request);

    boolean imgUpdate(MultipartFile multipartFile, Long user_id, HttpServletRequest request);

    boolean imgDelete(Long user_id, HttpServletRequest request);

    TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto);

    TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request);

}
