package com.project.review.product.repository;

import com.project.review.product.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productImgRepository extends JpaRepository<ProductImg, Long> {
}
