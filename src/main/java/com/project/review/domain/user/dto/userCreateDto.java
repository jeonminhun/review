package com.project.review.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userCreateDto {
    private String user_name;
    private String user_email;
    private String user_phoneNumber;
    private String user_password;
    private String user_password_check;
}
