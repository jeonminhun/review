package com.project.review.product.service;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.entity.*;
import com.project.review.product.repository.ReviewLikeRepository;
import com.project.review.product.repository.productReviewImgRepository;
import com.project.review.product.repository.productReviewRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.lang.String.valueOf;

@Slf4j
@Service
@AllArgsConstructor
public class productServiceImpl implements productService {
    private final productReviewRepository productReviewRepository;
    private final productReviewImgRepository productReviewImgRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    public boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request) {
        log.info("리뷰 생성 서비스 시작, 제품 id : " + reviewCreateDto.getProduct().getProduct_id() + " 유저 id : " + reviewCreateDto.getUser().getUser_id());
        try {
            int sum = reviewCreateDto.getCoef_rating() + reviewCreateDto.getDurability_rating() + reviewCreateDto.getQuality_rating() + reviewCreateDto.getDesign_rating();

            int total = sum / 4;

            Review review = Review.builder()
                    .user(reviewCreateDto.getUser())
                    .product(reviewCreateDto.getProduct())
                    .coef_rating(reviewCreateDto.getCoef_rating())
                    .durability_rating(reviewCreateDto.getDurability_rating())
                    .quality_rating(reviewCreateDto.getQuality_rating())
                    .design_rating(reviewCreateDto.getDesign_rating())
                    .total_rating(total)
                    .content(reviewCreateDto.getContent())
                    .build();

            review = productReviewRepository.save(review);

            int i = 1;

            for (MultipartFile file : files) {
                ReviewImg reviewImg = imgSave(file, request, review, valueOf(review.getReview_id())+"_"+valueOf(review.getUser().getUser_id())+"_"+i+".jpg");
                productReviewImgRepository.save(reviewImg);
                i++;
            }

            return true;
        } catch (Exception e) {
            log.info("리뷰 생성 실패, 제품 id : " + reviewCreateDto.getProduct().getProduct_id() + " 유저 id : " + reviewCreateDto.getUser().getUser_id());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request) {
        log.info("좋아요 추가 : ");
        try {
            ReviewLike reviewLike = ReviewLike.builder()
                    .review(reviewLikeDto.getReview())
                    .user(reviewLikeDto.getUser())
                    .review_ch(reviewLikeDto.getReview_ch())
                    .build();
            reviewLikeRepository.save(reviewLike);
            return true;
        } catch (Exception e) {
            log.info("좋아요 추가 실패 : ");
            e.printStackTrace();
            return false;
        }
    }

    private ReviewImg imgSave(MultipartFile files, HttpServletRequest request, Review review, String filename) {
        try {
            if (files != null) {
                Path uploadPath = Path.of("imgs", "review");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                log.info(filename);

                Path filepath = uploadPath.resolve(filename);
                Files.copy(files.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING /*같은 파일이름 있으면 덮어쓰기*/);

                ReviewImg reviewImg = ReviewImg.builder()
                        .review(review)
                        .review_img_name(filename)
                        .build();
                return reviewImg;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info("이미지 저장 오류");
            e.printStackTrace();
            return null;
        }
    }
}
