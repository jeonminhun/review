package com.project.review.product.repository;

import com.project.review.product.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productReviewRepository extends JpaRepository<Review, Long> {
}
