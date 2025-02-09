package com.project.review.product.service;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface productService {
    Product productInfo(Long product_id, HttpServletRequest request);

    boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewUpdate(reviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewDelete(reviewTotalDto reviewTotalDto, HttpServletRequest request);

    boolean reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request);


}
