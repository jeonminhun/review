package com.project.review.domain.product.repository;

import com.project.review.domain.product.entity.Save;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface saveRepository extends JpaRepository<Save, Long> { // 다음번엔 되도록이면 save는 안쓰고 wishList로 사용하는걸로

    @Transactional
    @Query("SELECT u FROM Save u WHERE u.user.id = :user_id and u.product.id = :product_id")
    Save findByUser_product_id(@Param("user_id") Long user_id, @Param("product_id") Long product_id);
    @Transactional
    @Query("SELECT u FROM Save u WHERE u.user.id = :user_id")
    List<Save> findByUser_id(@Param("user_id") Long user_id);

}
