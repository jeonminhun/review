package com.project.review.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter@Setter
public class productCreateDto {
    private int user_id;
    private int product_name;
    private int user_manu;
}
