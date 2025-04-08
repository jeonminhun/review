package com.project.review.category.service;

import com.project.review.category.dto.categoryReviewDto;
import com.project.review.category.entity.Category;
import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.dto.saveDto;
import com.project.review.product.entity.*;
import com.project.review.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface categoryService {
    List<categoryReviewDto> categoryReviewCount();
}
