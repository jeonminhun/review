package com.project.review.domain.product.dto;

import com.project.review.domain.product.entity.Review;
import com.project.review.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class ReviewLikeDto {
    private Review review;
    private User user;
    private int review_ch;
}
