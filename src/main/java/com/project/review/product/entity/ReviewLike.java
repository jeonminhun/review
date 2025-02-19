package com.project.review.product.entity;

import com.project.review.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "review_id" }) // 같은 유저가 같은 리뷰에 중복 좋아요 못 누르게
})
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_like_id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int review_ch;
}
