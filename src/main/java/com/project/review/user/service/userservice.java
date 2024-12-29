package com.project.review.user.service;

import com.project.review.user.dto.userCreateDto;

public interface userService {
    boolean checkPassWord(userCreateDto userCreateDto);

    boolean createUser(userCreateDto userCreateDto);

}
