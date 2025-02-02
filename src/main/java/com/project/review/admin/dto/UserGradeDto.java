package com.project.review.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGradeDto {

    private Long user_id;
    private String user_name;
    private String user_email;
    private String user_password;
    private int user_role;
    private String Change_role;



}
