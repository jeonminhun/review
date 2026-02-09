package com.project.review.domain.category.repository;

import com.project.review.domain.category.entity.Category;
import com.project.review.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface categoryRepository extends JpaRepository<Category,Long> {

    @Transactional
    @Query("SELECT u FROM Category u WHERE u.product.id = :product_id")
    Category findByProduct_id(@Param("product_id") Long product_id);

    @Transactional
    @Query("SELECT u.product FROM Category u WHERE u.category_no = :categoryCode")
    List<Product> findByCategory(@Param("categoryCode") int categoryCode);

    @Transactional
    @Query("SELECT count(u.product) FROM Category u WHERE u.category_no = :categoryCode")
    Long categoryReviewCount(@Param("categoryCode") int categoryCode);
}
