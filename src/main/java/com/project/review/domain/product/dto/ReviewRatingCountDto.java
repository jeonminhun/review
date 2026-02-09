package com.project.review.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ReviewRatingCountDto {
    Integer total_rating;
    Long count;
}
