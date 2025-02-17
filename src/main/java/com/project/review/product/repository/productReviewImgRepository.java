package com.project.review.product.repository;

import com.project.review.product.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface productReviewImgRepository extends JpaRepository<ReviewImg, Long> {

    @Transactional
    @Query("SELECT u FROM ReviewImg u Left join review r ON u.review.review_id = r.review_id where r.product.product_id = :product_id")
    List<ReviewImg> findByProduct_id(@Param("product_id") Long product_id);

    @Transactional
    @Query("SELECT u FROM ReviewImg u where u.review.review_id = :review_id")
    List<ReviewImg> findByReview_id(@Param("review_id") Long review_id);
}
