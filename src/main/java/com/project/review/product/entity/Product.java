package com.project.review.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "product")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private String product_manu;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_coef_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_durability_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_quality_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_design_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_total_rating;

    @Setter
    @Transient // DB 에는 없지만 조회용 필드로 활용
    private boolean isSaved;
}
