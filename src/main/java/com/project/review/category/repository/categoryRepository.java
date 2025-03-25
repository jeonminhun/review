package com.project.review.category.repository;

import com.project.review.category.entity.Category;
import com.project.review.product.entity.ProductImg;
import com.project.review.user.entity.UserImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface categoryRepository extends JpaRepository<Category,Long> {

    @Transactional
    @Query("SELECT u FROM Category u WHERE u.product.id = :product_id")
    Category findByProduct_id(@Param("product_id") Long product_id);
}
