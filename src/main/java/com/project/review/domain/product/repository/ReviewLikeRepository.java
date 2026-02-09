package com.project.review.domain.product.repository;

import com.project.review.domain.product.entity.Review;
import com.project.review.domain.product.entity.ReviewLike;
import com.project.review.domain.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    @Query("select count(u) from ReviewLike u where u.review.review_id = :review_id")
    @Transactional
    Long countByReview(@Param("review_id") Long review_id);


    @Query("SELECT rl FROM ReviewLike rl WHERE rl.user.user_id = :userId AND rl.review.review_id = :reviewId")
    ReviewLike findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    boolean existsByReviewAndUser(Review review, User user);
}
