package com.project.review.product.repository;

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

    @Modifying
    @Transactional
    @Query("SELECT u FROM Review u WHERE u.product.id = :product_id")
    List<Review> findAllProduct(@Param("product_id") Long product_id);
}
