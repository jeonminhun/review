package com.project.review.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_img")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_img_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private product product;

    @Column(nullable = false)
    private String product_img_name;


}
