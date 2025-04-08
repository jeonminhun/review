package com.project.review.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateDto {
    private Long user_id;
    private String user_name;
    private String user_info;
    private String user_phoneNumber;
    private String user_nickName;
}
