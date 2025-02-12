package com.project.review.product.repository;

import com.project.review.product.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface productImgRepository extends JpaRepository<ProductImg, Long> {
    @Transactional
    @Query("SELECT u FROM ProductImg u WHERE u.product.id = :product_id")
    ProductImg findByProduct_id(@Param("product_id") Long product_id);

}
