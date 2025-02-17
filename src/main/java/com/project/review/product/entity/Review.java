package com.project.review.product.entity;

import com.project.review.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int coef_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int durability_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int quality_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int design_rating;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int total_rating;

    @Column(nullable = false)
    private String content;

    // 🔥 ReviewLike 삭제 설정 추가
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

}
