package com.project.review.product.service;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.ReviewTotalDto;
import com.project.review.product.dto.saveDto;
import com.project.review.product.entity.*;
import com.project.review.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface productService {
    List<Product> productAll();

    List<Product> productRank();

    List<Product> productSlide();

    Product productInfo(Long product_id, HttpServletRequest request);

    Product productInfoLogin(Long product_id, Long user_id);

    Page<Product> productSearch(String query, Pageable pageable);

    Page<Product>  productCategory(String category, Pageable pageable);

    List<Save> saveInfo(Long user_id, HttpServletRequest request);

    ProductImg productImgInfo(Long product_id);

    List<Review> ReviewInfo(Long product_id, HttpServletRequest request);

    List<Review> myReview(Long user_id, HttpServletRequest request);

    List<Review> ReviewAll();

    List<Product> ProductAll();

    List<ProductImg> ProductImgAll();

    List<Review> ReviewInfo_Login(Long product_id, User user);

    List<ReviewImg> ReviewImgInfo(Long product_id);

    Map<Integer, Long> RatingCount(Long product_id);

    Map<String, Object> chartData(Long product_id);

    boolean productSave(saveDto saveDto, HttpServletRequest request);

    boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewUpdate(ReviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request);

    Long reviewDelete(Long review_id, HttpServletRequest request);

    Review reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request);


}
