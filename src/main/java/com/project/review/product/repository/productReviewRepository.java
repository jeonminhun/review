package com.project.review.product.repository;

import com.project.review.product.dto.ReviewRatingCountDto;
import com.project.review.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface productReviewRepository extends JpaRepository<Review, Long> {

    @Transactional
    @Query("SELECT u FROM Review u WHERE u.product.id = :product_id")
    List<Review> findProduct_id(@Param("product_id") Long product_id);

    @Transactional
    @Query("SELECT u FROM Review u WHERE u.user.id = :user_id")
    List<Review> findByUser_id(@Param("user_id") Long user_id);

    @Transactional
    @Query("SELECT new com.project.review.product.dto.ReviewRatingCountDto(r.total_rating, COUNT(r)) " +
            "FROM Review r " +
            "WHERE r.product.product_id = :product_id " +
            "GROUP BY r.total_rating " +
            "ORDER BY r.total_rating ASC")
    List<ReviewRatingCountDto> ratingCount(@Param("product_id") Long product_id);
}
