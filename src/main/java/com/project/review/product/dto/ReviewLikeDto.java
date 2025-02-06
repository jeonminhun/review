package com.project.review.product.dto;

import com.project.review.product.entity.Review;
import com.project.review.user.entity.User;
import jakarta.persistence.*;
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
