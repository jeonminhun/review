package com.project.review.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class productDto {
    private Long product_id;
    private String product_name;
    private String product_manu;
    private int product_rating;
}
