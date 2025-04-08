package com.project.review.product.dto;

import com.project.review.product.entity.Review;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class ReviewImgDto {
    private Long review_img_id;
    private Review review;
    private String review_img_name;
}
