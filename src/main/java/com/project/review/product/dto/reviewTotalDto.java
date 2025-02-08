package com.project.review.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class reviewTotalDto {
    private ReviewCreateDto reviewCreateDto;
    private ReviewImgDto[] deleteImgDto;
}
