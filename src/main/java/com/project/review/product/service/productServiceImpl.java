package com.project.review.product.service;

import com.project.review.category.dto.categoryEnum;
import com.project.review.category.entity.Category;
import com.project.review.category.repository.categoryRepository;
import com.project.review.product.dto.*;
import com.project.review.product.entity.*;
import com.project.review.product.repository.*;
import com.project.review.user.entity.User;
import com.project.review.user.jwt.TokenProvider;
import com.project.review.user.repository.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.lang.String.valueOf;

@Slf4j
@Service
@AllArgsConstructor
public class productServiceImpl implements productService {
    private final productRepository productRepository;
    private final productImgRepository productImgRepository;
    private final productReviewRepository productReviewRepository;
    private final productReviewImgRepository productReviewImgRepository;
    private final userRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final categoryRepository categoryRepository;
    private final saveRepository saveRepository;
    private final TokenProvider tokenProvider;


    @Override
    public List<Product> productAll() {
        List<Product> all = productRepository.findAll();
        return all;
    }

    @Override
    public List<Product> productRank() {

        return productRepository.productRank(PageRequest.of(0, 8));
    }

    @Override
    public List<Product> productSlide() {
        return productRepository.productRank(PageRequest.of(0, 4));
    }

    @Override
    public Product productInfo(Long product_id, HttpServletRequest request) {
        Product product = productRepository.findById(product_id).get();
        return product;
    }

    @Override
    public Product productInfoLogin(Long product_id, Long user_id) {

        Product product = productRepository.findById(product_id).get();

        Save save = saveRepository.findByUser_product_id(user_id, product_id);
        product.setSaved(false);
        if (save != null) {
            product.setSaved(true);
        }
        return product;
    }

    @Override
    public Page<Product> productSearch(String query, Pageable pageable) {
        Page<Product>  products = productRepository.productSearchName(query, pageable);

        for (Product product : products) {
            Category category = categoryRepository.findByProduct_id(product.getProduct_id());
            String categoryByCode = categoryEnum.getCategoryByCode(category.getCategory_no());
            product.setCategory(categoryByCode);
        }

        return products;
    }

    @Override
    public Page<Product>  productCategory(String category, Pageable pageable) {
        int codeByCategory = categoryEnum.getCodeByCategory(category);
        Page<Product>  products = productRepository.findByCategory(codeByCategory, pageable);

        for (Product product : products) {
            Category forCategory = categoryRepository.findByProduct_id(product.getProduct_id());
            String categoryByCode = categoryEnum.getCategoryByCode(forCategory.getCategory_no());
            product.setCategory(categoryByCode);
        }
        return products;
    }

    @Override
    public List<Save> saveInfo(Long user_id, HttpServletRequest request) {
        List<Save> saves = saveRepository.findByUser_id(user_id);
        return saves;
    }



    @Override
    public ProductImg productImgInfo(Long product_id) {
        return productImgRepository.findByProduct_id(product_id);
    }
    @Override
    public List<Review> ReviewInfo(Long product_id, HttpServletRequest request) {

        List<Review> reviews = productReviewRepository.findProduct_id(product_id);

        for (Review review : reviews) {
            long likeCount = reviewLikeRepository.countByReview(review.getReview_id());
            review.setLikeCount(likeCount);
        }
        return reviews;
    }

    @Override
    public List<Review> myReview(Long user_id, HttpServletRequest request) {
        return productReviewRepository.findByUser_id(user_id);
    }

    @Override
    public List<Review> ReviewAll() {
        List<Review> reviews = productReviewRepository.findAll();

        for (Review review : reviews) {
            long likeCount = reviewLikeRepository.countByReview(review.getReview_id());
            review.setLikeCount(likeCount);
        }
        return reviews;
    }

    @Override
    public List<Product> ProductAll() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            Category category = categoryRepository.findByProduct_id(product.getProduct_id());
            String categoryByCode = categoryEnum.getCategoryByCode(category.getCategory_no());
            product.setCategory(categoryByCode);
        }

        return products;
    }

    @Override
    public List<ProductImg> ProductImgAll() {
        return productImgRepository.findAll();
    }

    @Override
    public List<Review> ReviewInfo_Login(Long product_id, User user) {

        List<Review> reviews = productReviewRepository.findProduct_id(product_id);

        for (Review review : reviews) {
            long likeCount = reviewLikeRepository.countByReview(review.getReview_id());
            review.setLikeCount(likeCount);

            // 현재 로그인한 사용자가 이 리뷰에 좋아요를 눌렀는지 확인
            boolean isLiked = reviewLikeRepository.existsByReviewAndUser(review, user);
            review.setLiked(isLiked);
        }
        return reviews;
    }

    @Override
    public List<ReviewImg> ReviewImgInfo(Long product_id) {
        return productReviewImgRepository.findByProduct_id(product_id);
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
    public boolean productSave(saveDto saveDto, HttpServletRequest request) {
        User user = User.builder().user_id(saveDto.getUser_id()).build();
        Product product = Product.builder().product_id(saveDto.getProduct_id()).build();
        Save save = Save.builder().user(user).product(product).build();

        Save save1 = saveRepository.findByUser_product_id(saveDto.getUser_id(), saveDto.getProduct_id());

        if (save1 == null) {
            saveRepository.save(save);
        } else {
            saveRepository.delete(save1);
        }



        return true;
    }

    @Override
    public boolean reviewCreate(ReviewCreateDto reviewCreateDto, MultipartFile[] files, HttpServletRequest request) {
        log.info("리뷰 생성 서비스 시작, 제품 id : " + reviewCreateDto.getProduct_id() + " 유저 id : " + reviewCreateDto.getUser_id());
        try {
            int total = getTotal(reviewCreateDto);

            User user = userRepository.findById(reviewCreateDto.getUser_id()).get();
            Product product = productRepository.findById(reviewCreateDto.getProduct_id()).get();

            Review review = Review.builder()
                    .user(user)
                    .product(product)
                    .coef_rating(reviewCreateDto.getCoef_rating())
                    .durability_rating(reviewCreateDto.getDurability_rating())
                    .quality_rating(reviewCreateDto.getQuality_rating())
                    .design_rating(reviewCreateDto.getDesign_rating())
                    .total_rating(total)
                    .content(reviewCreateDto.getContent())
                    .created_date(new Date())
                    .build();

            review = productReviewRepository.save(review);

            productRatingUpdate(reviewCreateDto.getProduct_id());

            if (files != null) {
                for (MultipartFile file : files) {
                    String fileName = System.currentTimeMillis() + "_" + valueOf(review.getReview_id()) + "_" + valueOf(review.getUser().getUser_id()) + ".jpg";
                    ReviewImg reviewImg = imgSave(file, review, fileName);
                    productReviewImgRepository.save(reviewImg);
                }
            }

            return true;
        } catch (Exception e) {
            log.info("리뷰 생성 실패, 제품 id : " + reviewCreateDto.getProduct_id() + " 유저 id : " + reviewCreateDto.getUser_id());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean reviewUpdate(reviewTotalDto reviewTotalDto, MultipartFile[] files, HttpServletRequest request) {

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setCoef_rating(reviewTotalDto.getCoefRating());
        reviewCreateDto.setDurability_rating(reviewTotalDto.getDurabilityRating());
        reviewCreateDto.setQuality_rating(reviewTotalDto.getQualityRating());
        reviewCreateDto.setDesign_rating(reviewTotalDto.getDesignRating());

        log.info("리뷰 수정 서비스 : " + reviewTotalDto.getReviewId());
        try {
            if (Self_identification(request, reviewTotalDto.getUserId())) {
                int total = getTotal(reviewCreateDto);

                Review review = productReviewRepository.findById(reviewTotalDto.getReviewId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다: " + reviewTotalDto.getReviewId()));

                // ✅ updateReview() 메서드 호출 (Dirty Checking 유도)
                review.updateReview(
                        reviewTotalDto.getCoefRating(),
                        reviewTotalDto.getDurabilityRating(),
                        reviewTotalDto.getQualityRating(),
                        reviewTotalDto.getDesignRating(),
                        total,
                        reviewTotalDto.getUpdateContent()
                );

                log.info("이미지 삭제 체크 ");

                if (reviewTotalDto.getDeleteImgIds() != null) {
                    for (Long deleteImgId : reviewTotalDto.getDeleteImgIds()) {
                        ReviewImg reviewImg = productReviewImgRepository.findById(deleteImgId).get();
                        imgDelete(reviewImg);
                        productReviewImgRepository.delete(reviewImg);
                    }
                }

                if (files[0] != null) {
                    log.info("파일 저장 시작 로그");
                    for (MultipartFile file : files) {
                        if (!file.isEmpty()) {
                            String fileName = System.currentTimeMillis() + "_" + valueOf(review.getReview_id()) + "_" + valueOf(review.getUser().getUser_id()) + ".jpg";
                            ReviewImg reviewImg = imgSave(file, review, fileName);
                            productReviewImgRepository.save(reviewImg);
                        }else {
                            log.info("빈 파일 발견: 처리하지 않음");
                        }
                    }
                }
                log.info("리뷰 수정 성공 로그");
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
    public Long reviewDelete(Long review_id, HttpServletRequest request) {
        Review review = productReviewRepository.findById(review_id).get();
        try {
            List<ReviewImg> reviewImgs = productReviewImgRepository.findByReview_id(review_id);
            log.info("리뷰 삭제 서비스 : " + review.getReview_id());

            if (Self_identification(request, review.getUser().getUser_id())) {
                if (reviewImgs != null) {
                    for (ReviewImg reviewImg : reviewImgs) {
                        imgDelete(reviewImg);
                        productReviewImgRepository.delete(reviewImg);
                    }
                }
                productReviewRepository.delete(review);
                return review.getProduct().getProduct_id();
            } else {
                log.info("본인 인증 실패");
                return null;
            }

        } catch (Exception e) {
            log.info("리뷰 삭제 실패 : " + review.getReview_id());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Review reviewLike(ReviewLikeDto reviewLikeDto, HttpServletRequest request) {
        log.info("좋아요 추가 : ");
        try {
            ReviewLike reviewLike = ReviewLike.builder()
                    .review(reviewLikeDto.getReview())
                    .user(reviewLikeDto.getUser())
                    .review_ch(reviewLikeDto.getReview_ch())
                    .build();

            boolean chUser = reviewLikeRepository.existsByReviewAndUser(reviewLike.getReview(), reviewLike.getUser());
            if (chUser) {
                log.info("리뷰 좋아요 삭제 : ");
                ReviewLike like = reviewLikeRepository.findByUserIdAndReviewId(reviewLike.getUser().getUser_id(), reviewLike.getReview().getReview_id());
                reviewLikeRepository.delete(like);
            } else {

                ReviewLike save = reviewLikeRepository.save(reviewLike);
            }


            boolean chUser2 = reviewLikeRepository.existsByReviewAndUser(reviewLike.getReview(), reviewLike.getUser());
            log.info("T/F : "+chUser2);
            long likeCount = reviewLikeRepository.countByReview(reviewLike.getReview().getReview_id());

            reviewLike.getReview().setLikeCount(likeCount);
            reviewLike.getReview().setLiked(chUser2);


            return reviewLike.getReview();
        } catch (Exception e) {
            log.info("좋아요 추가 실패 : ");
            e.printStackTrace();
            return null;
        }
    }
    // 제품 별점 업데이트
    private void productRatingUpdate(Long product_id) {
        productRepository.productRatingUpdate(product_id);
    }

    private boolean Self_identification(HttpServletRequest request, Long user_id) {
        User user = userRepository.findById(user_id).get();
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

    private void imgDelete(ReviewImg reviewImg) {
        try {
            Path uploadPath = Path.of("src","main","resources","static","imgs", "review");
            Path filepath = uploadPath.resolve(reviewImg.getReview_img_name());
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
        }
    }

    private ReviewImg imgSave(MultipartFile files, Review review, String filename) {
        try {
            if (files != null) {
                Path uploadPath = Path.of("src","main","resources","static","imgs", "review");

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
