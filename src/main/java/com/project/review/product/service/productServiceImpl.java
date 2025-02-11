package com.project.review.product.service;

import com.project.review.product.dto.*;
import com.project.review.product.entity.*;
import com.project.review.product.repository.ReviewLikeRepository;
import com.project.review.product.repository.productRepository;
import com.project.review.product.repository.productReviewImgRepository;
import com.project.review.product.repository.productReviewRepository;
import com.project.review.user.entity.User;
import com.project.review.user.jwt.TokenProvider;
import com.project.review.user.repository.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

@Slf4j
@Service
@AllArgsConstructor
public class productServiceImpl implements productService {
    private final productRepository productRepository;
    private final productReviewRepository productReviewRepository;
    private final productReviewImgRepository productReviewImgRepository;
    private final userRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final TokenProvider tokenProvider;


    @Override
    public Product productInfo(Long product_id, HttpServletRequest request) {
        return productRepository.findById(product_id).get();
    }

    @Override
    public List<Review> ReviewInfo(Long product_id, HttpServletRequest request) {
        return productReviewRepository.findAllProduct(product_id);
    }

    @Override
    public Map<Integer, Long> RatingCount(Long product_id) {
        List<ReviewRatingCountDto> ratingCount = productReviewRepository.ratingCount(product_id);

        Map<Integer, Long> ratingMap = new HashMap<>();
        for (ReviewRatingCountDto dto : ratingCount) {
            ratingMap.put(dto.getTotal_rating(), dto.getCount());
        }

        // 별점이 없는 경우 0으로 채우기
        for (int i = 1; i <= 5; i++) {
            ratingMap.putIfAbsent(i, 0L);
        }

        return ratingMap;
    }

    @Override
    public Map<String, Object> chartData(Long product_id) {

        Map<String, Object> chartData = new HashMap<>();
        Product product = productRepository.findById(product_id).get();
        Map<Integer, Long> ratingCount = RatingCount(product_id);
        chartData.put("data", Arrays.asList(product.getProduct_total_rating(), product.getProduct_coef_rating(), product.getProduct_durability_rating(), product.getProduct_quality_rating(), product.getProduct_design_rating())); // 차트에 들어갈 데이터
        chartData.put("labels", Arrays.asList("총 별점", "가성비 별점", "내구성 별점", "품질 별점", "디자인 별점")); // X축 레이블
        chartData.put("total", Arrays.asList(ratingCount.get(5), ratingCount.get(4), ratingCount.get(3), ratingCount.get(2), ratingCount.get(1))); // 차트에 들어갈 데이터
        return chartData;
    }

    @Override
    public boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request) {
        log.info("리뷰 생성 서비스 시작, 제품 id : " + reviewCreateDto.getProduct().getProduct_id() + " 유저 id : " + reviewCreateDto.getUser().getUser_id());
        try {
            int total = getTotal(reviewCreateDto);

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

            productRatingUpdate(reviewCreateDto.getProduct().getProduct_id());

            if (files != null) {
                for (MultipartFile file : files) {
                    String fileName = System.currentTimeMillis() + "_" + valueOf(review.getReview_id()) + "_" + valueOf(review.getUser().getUser_id()) + ".jpg";
                    ReviewImg reviewImg = imgSave(file, request, review, fileName);
                    productReviewImgRepository.save(reviewImg);
                }
            }

            return true;
        } catch (Exception e) {
            log.info("리뷰 생성 실패, 제품 id : " + reviewCreateDto.getProduct().getProduct_id() + " 유저 id : " + reviewCreateDto.getUser().getUser_id());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reviewUpdate(reviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request) {
        ReviewCreateDto reviewCreateDto = reviewTotalDto.getReviewCreateDto();
        ReviewImgDto[] deleteImgDto = reviewTotalDto.getDeleteImgDto();
        log.info("리뷰 수정 서비스 : " + reviewCreateDto.getReview_id());
        try {
            if (Self_identification(request, reviewCreateDto)) {
                int total = getTotal(reviewCreateDto);
                Review review = Review.builder()
                        .review_id(reviewCreateDto.getReview_id())
                        .user(reviewCreateDto.getUser())
                        .product(reviewCreateDto.getProduct())
                        .coef_rating(reviewCreateDto.getCoef_rating())
                        .durability_rating(reviewCreateDto.getDurability_rating())
                        .quality_rating(reviewCreateDto.getQuality_rating())
                        .design_rating(reviewCreateDto.getDesign_rating())
                        .total_rating(total)
                        .content(reviewCreateDto.getContent())
                        .build();

                productReviewRepository.save(review);

                if (deleteImgDto != null) {
                    for (ReviewImgDto reviewImgDto : deleteImgDto) {
                        imgDelete(reviewImgDto);
                        ReviewImg reviewImg = ReviewImg.builder()
                                .review_img_id(reviewImgDto.getReview_img_id())
                                .build();
                        productReviewImgRepository.delete(reviewImg);
                    }
                }

                if (files != null) {
                    for (MultipartFile file : files) {
                        String fileName = System.currentTimeMillis() + "_" + valueOf(review.getReview_id()) + "_" + valueOf(review.getUser().getUser_id()) + ".jpg";
                        ReviewImg reviewImg = imgSave(file, request, review, fileName);
                        productReviewImgRepository.save(reviewImg);
                    }
                }
                return true;
            } else {
                log.info("본인 인증 실패");
                return false;
            }
        } catch (Exception e) {
            log.info("리뷰 수정 실패 : " + reviewCreateDto.getReview_id());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean reviewDelete(reviewTotalDto reviewTotalDto, HttpServletRequest request) {
        ReviewCreateDto reviewCreateDto = reviewTotalDto.getReviewCreateDto();
        ReviewImgDto[] deleteImgDto = reviewTotalDto.getDeleteImgDto();
        log.info("리뷰 삭제 서비스 : " + reviewCreateDto.getReview_id());
        try {
            if (Self_identification(request, reviewCreateDto)) {
                if (deleteImgDto != null) {
                    for (ReviewImgDto reviewImgDto : deleteImgDto) {
                        imgDelete(reviewImgDto);
                        ReviewImg reviewImg = ReviewImg.builder()
                                .review_img_id(reviewImgDto.getReview_img_id())
                                .build();
                        productReviewImgRepository.delete(reviewImg);
                    }
                }

                Review review = Review.builder()
                        .review_id(reviewCreateDto.getReview_id())
                        .build();

                productReviewRepository.delete(review);
                return true;
            } else {
                log.info("본인 인증 실패");
                return false;
            }

        } catch (Exception e) {
            log.info("리뷰 삭제 실패 : " + reviewCreateDto.getReview_id());
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
    // 제품 별점 업데이트
    private void productRatingUpdate(Long product_id) {
        productRepository.productRatingUpdate(product_id);
    }

    private boolean Self_identification(HttpServletRequest request, ReviewCreateDto reviewCreateDto) {
        User user = userRepository.findById(reviewCreateDto.getUser().getUser_id()).get();
        // 권한 체크 해서 관리자 일경우 그냥 허용하기

        if (tokenProvider.AuthenticationCheck(request)) {
            return true;
        } else if (user.getUserEmail().equals(tokenProvider.getUserIdFromToken(request))) {
            return true;
        } else {
            return false;
        }
    }

    private static int getTotal(ReviewCreateDto reviewCreateDto) {
        int sum = reviewCreateDto.getCoef_rating() + reviewCreateDto.getDurability_rating() + reviewCreateDto.getQuality_rating() + reviewCreateDto.getDesign_rating();

        int total = sum / 4;
        return total;
    }

    private void imgDelete(ReviewImgDto reviewImgDto) {
        try {
            Path uploadPath = Path.of("imgs", "review");
            Path filepath = uploadPath.resolve(reviewImgDto.getReview_img_name());
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
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
