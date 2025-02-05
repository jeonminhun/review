package com.project.review.product.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

@Slf4j
@Getter @Setter
public class productDto {
    private Long product_id;
    private String product_name;
    private String product_manu;
    private int product_coef_rating;
    private int product_durability_rating;
    private int product_quality_rating;
    private int product_design_rating;
    private int product_total_rating;
}
