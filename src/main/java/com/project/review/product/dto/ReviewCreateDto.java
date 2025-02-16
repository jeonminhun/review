package com.project.review.product.dto;

import com.project.review.product.entity.Product;
import com.project.review.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

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
