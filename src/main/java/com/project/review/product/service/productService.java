package com.project.review.product.service;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.ReviewRatingCountDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.Review;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface productService {
    Product productInfo(Long product_id, HttpServletRequest request);

    List<Review> ReviewInfo(Long product_id, HttpServletRequest request);

    Map<Integer, Long> RatingCount(Long product_id);

    Map<String, Object> chartData(Long product_id);

    boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewUpdate(reviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewDelete(reviewTotalDto reviewTotalDto, HttpServletRequest request);

    boolean reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request);


}
