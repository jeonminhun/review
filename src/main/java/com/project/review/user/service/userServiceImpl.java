package com.project.review.user.service;

import com.project.review.user.dto.userCreateDto;
import com.project.review.user.entity.User;
import com.project.review.user.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class userServiceImpl implements userService {

    private final userRepository userRepository;
    private final PasswordEncoder encoder;

    @Override

    public boolean checkPassWord(userCreateDto userCreateDto) {
        return userCreateDto.getUser_password().equals(userCreateDto.getUser_password_check());
    }

    @Override
    public boolean createUser(userCreateDto userCreateDto) {
        String password = encoder.encode(userCreateDto.getUser_email() + userCreateDto.getUser_password());
        User user = User.builder()
                .user_email(userCreateDto.getUser_email())
                .user_name(userCreateDto.getUser_name())
                .user_password(password)
                .user_role(1)
                .build();
        userRepository.save(user);
        return true;
    }
}
