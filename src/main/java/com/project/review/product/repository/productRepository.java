package com.project.review.product.repository;

import com.project.review.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface productRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET " +
            "p.product_total_rating = (SELECT AVG(r.total_rating) FROM Review r WHERE r.product.product_id =:product_id), " +
            "p.product_coef_rating = (SELECT AVG(r.coef_rating) FROM Review r WHERE r.product.product_id =:product_id)," +
            "p.product_durability_rating = (SELECT AVG(r.durability_rating) FROM Review r WHERE r.product.product_id =:product_id)," +
            "p.product_quality_rating = (SELECT AVG(r.quality_rating) FROM Review r WHERE r.product.product_id =:product_id)," +
            "p.product_design_rating = (SELECT AVG(r.design_rating) FROM Review r WHERE r.product.product_id =:product_id)" +
            "WHERE p.product_id =:product_id")
    int productRatingUpdate(@Param("product_id") Long product_id);

    @Transactional
    @Query("SELECT p FROM Product p LEFT JOIN Review r ON r.product.id = p.id GROUP BY p.id ORDER BY COUNT(r) DESC")
    List<Product> productRank(Pageable pageable);

    @Transactional
    @Query("select u from Product u where u.product_name LIKE %:query%")
    Page<Product> productSearchName(@Param("query") String query, Pageable pageable);

    @Transactional
    @Query("SELECT u.product FROM Product p left outer join Category u on u.category_no = :categoryCode")
    Page<Product>  findByCategory(@Param("categoryCode") int categoryCode, Pageable pageable);
}
