package com.project.review.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class reviewTotalDto {
    private Long reviewId;
    private Long userId;
    private Long productId;
    private int coefRating;
    private int durabilityRating;
    private int qualityRating;
    private int designRating;
    private String updateContent;

    // 삭제할 이미지 ID 목록
    private Long[] deleteImgIds;
}
