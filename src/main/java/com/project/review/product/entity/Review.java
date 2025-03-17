package com.project.review.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.review.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Date;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
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

    @Column(nullable = false)
    private Date created_date;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ReviewImg> reviewImgs = new ArrayList<>();

    // 🔥 ReviewLike 삭제 설정 추가
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    // 좋아요 개수를 설정하는 메서드 추가
    @Setter
    @Transient // DB 컬럼에는 없지만, 조회용 필드로 사용
    private long likeCount;

    @Setter
    @Transient // DB 에는 없지만 조회용 필드로 활용
    private boolean isLiked;


    // ✅ setter 대신 업데이트 메서드 추가 (변경 감지를 유도)
    public void updateReview(int coef_rating, int durability_rating, int quality_rating, int design_rating, int total_rating, String content) {
        this.coef_rating = coef_rating;
        this.durability_rating = durability_rating;
        this.quality_rating = quality_rating;
        this.design_rating = design_rating;
        this.total_rating = total_rating;
        this.content = content;
    }
}
