package com.project.review.user.service;

import com.project.review.user.dto.MemberRequestDto;
import com.project.review.user.dto.TokenDto;
import com.project.review.user.dto.TokenRequestDto;
import com.project.review.user.dto.userCreateDto;
import jakarta.servlet.http.HttpServletRequest;

public interface userService {

    boolean test(MemberRequestDto memberRequestDto);

    boolean checkPassWord(userCreateDto userCreateDto);

    boolean createUser(userCreateDto userCreateDto);

    TokenDto login(HttpServletRequest request, MemberRequestDto memberRequestDto);

    TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest request);

}
