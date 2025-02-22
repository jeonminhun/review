package com.project.review.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGradeDto {

    private Long user_id;
    private String user_name;
    @JsonProperty("Change_role") // 이름 매핑
    private int Change_role;



}
