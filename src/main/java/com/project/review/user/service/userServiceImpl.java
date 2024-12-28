package com.project.review.user.service;

import com.project.review.user.dto.userCreateDto;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    @Override
    public boolean checkPassWord(userCreateDto userCreateDto) {
        return userCreateDto.getUser_password().equals(userCreateDto.getUser_password_check());
    }

    @Override
    public String createUser(userCreateDto userCreateDto) {
        return "";
    }
}
