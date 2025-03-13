package com.project.review.admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class adminProductUpdateDto {
    private Long product_id;
    private String product_name;
    private String product_manu;
    private int product_coef_rating;
    private int product_durability_rating;
    private int product_quality_rating;
    private int product_design_rating;
    private int product_total_rating;
}
