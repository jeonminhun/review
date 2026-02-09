package com.project.review.domain.category.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class categoryReviewDto {
    private String category;
    private Long reviewCount;

}
