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
public class product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private String product_manu;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int product_rating;
}
