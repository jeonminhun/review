package com.project.review.product.dto;

import com.project.review.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class productImgDto {
    private Long product_img_id;
    private Product product;
    private String product_img_name;
}
