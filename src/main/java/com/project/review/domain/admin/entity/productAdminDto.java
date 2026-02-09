package com.project.review.domain.admin.entity;

import com.project.review.domain.product.dto.productDto;
import com.project.review.domain.product.dto.productImgDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class productAdminDto {
    private productDto productDto;
    private productImgDto productImgDto;
}
