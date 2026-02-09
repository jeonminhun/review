package com.project.review.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class productCreateDto {
    private String product_name;
    private String product_manu;
    private String category_no;
}
