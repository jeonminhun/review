package com.project.review.category.entity;

import com.project.review.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int category_no;

    public void updateCategory(int category_no) {
        this.category_no = category_no;
    }
}
