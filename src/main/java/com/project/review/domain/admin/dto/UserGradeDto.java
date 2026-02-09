package com.project.review.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGradeDto {

    private Long user_id;
    private String user_name;
    @JsonProperty("Change_role")
    private int Change_role;



}
