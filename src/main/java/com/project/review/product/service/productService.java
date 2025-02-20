package com.project.review.product.service;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.entity.*;
import com.project.review.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface productService {
    Product productInfo(Long product_id, HttpServletRequest request);

    ProductImg productImgInfo(Long product_id);

    List<Review> ReviewInfo(Long product_id, HttpServletRequest request);

    List<Review> ReviewInfo_Login(Long product_id, User user);

    List<ReviewImg> ReviewImgInfo(Long product_id);

    Map<Integer, Long> RatingCount(Long product_id);

    Map<String, Object> chartData(Long product_id);

    boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request);

    boolean reviewUpdate(reviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request);

    Long reviewDelete(Long review_id, HttpServletRequest request);

    Review reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request);


}
