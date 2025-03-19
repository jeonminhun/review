package com.project.review.product.repository;

import com.project.review.product.entity.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface saveRepository extends JpaRepository<Save, Long> {

    @Transactional
    @Query("SELECT u FROM Save u WHERE u.user.id = :user_id and u.product.id = :product_id")
    Save findByUser_product_id(@Param("user_id") Long user_id, @Param("product_id") Long product_id);
    @Transactional
    @Query("SELECT u FROM Save u WHERE u.user.id = :user_id")
    List<Save> findByUser_id(@Param("user_id") Long user_id);

}
