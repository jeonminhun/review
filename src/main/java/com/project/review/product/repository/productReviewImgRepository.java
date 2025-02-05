package com.project.review.product.repository;

import com.project.review.product.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
