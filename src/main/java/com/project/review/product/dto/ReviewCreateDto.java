package com.project.review.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ReviewCreateDto {
    private Long review_id;
    private Long user_id;
    private Long product_id;
    private int coef_rating;
    private int durability_rating;
    private int quality_rating;
    private int design_rating;
    private String content;
}
