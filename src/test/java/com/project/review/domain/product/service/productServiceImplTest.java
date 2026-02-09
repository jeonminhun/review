package com.project.review.domain.product.service;

import com.project.review.domain.category.entity.Category;
import com.project.review.domain.category.repository.categoryRepository;
import com.project.review.domain.product.dto.*;
import com.project.review.domain.product.entity.*;
import com.project.review.domain.product.repository.*;
import com.project.review.domain.user.entity.User;
import com.project.review.domain.user.jwt.TokenProvider;
import com.project.review.domain.user.repository.userRepository;
import com.project.review.global.util.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class productServiceImplTest {

    @InjectMocks
    private productServiceImpl  productService;
    @Mock
    private productRepository productRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private productImgRepository productImgRepository;
    @Mock
    private productReviewRepository productReviewRepository;
    @Mock
    private productReviewImgRepository productReviewImgRepository;
    @Mock
    private userRepository userRepository;
    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    @Mock
    private categoryRepository categoryRepository;
    @Mock
    private saveRepository saveRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private FileService fileService;

    @Test
    void productAllTest() {
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest2").product_name("test2").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest3").product_name("test3").build();

        List<Product> productAll = new ArrayList<>();
        productAll.add(product1);
        productAll.add(product2);
        productAll.add(product3);

        given(productRepository.findAll()).willReturn(productAll);

        List<Product> products = productService.productAll();

        assertThat(products.get(0).getProduct_id()).isEqualTo(1L);
    }

    @Test
    void productRankTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest2").product_name("test2").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest3").product_name("test3").build();

        List<Product> productAll = new ArrayList<>();
        productAll.add(product1);
        productAll.add(product2);
        productAll.add(product3);

        given(productRepository.productRank(any())).willReturn(productAll);


        List<Product> products = productService.productRank();

        assertThat(products.get(0).getProduct_id()).isEqualTo(1L);
    }
    @Test
    void productSlideTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest2").product_name("test2").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest3").product_name("test3").build();

        List<Product> productAll = new ArrayList<>();
        productAll.add(product1);
        productAll.add(product2);
        productAll.add(product3);

        given(productRepository.productRank(any())).willReturn(productAll);


        List<Product> products = productService.productSlide();

        assertThat(products.get(0).getProduct_id()).isEqualTo(1L);
    }

    @Test
    void productInfoTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        productService.productInfo(1L,request);

        assertThat(product1.getProduct_id()).isEqualTo(1L);
        assertThat(product1.getProduct_name()).isEqualTo("test1");
        assertThat(product1.getProduct_manu()).isEqualTo("testtest1");
    }
    @Test
    void productInfoLoginTest(){
        Product product1 =Product.builder().product_id(10L).product_manu("testtest1").product_name("test1").build();
        User testuser = User.builder()
                .user_id(1L).userEmail("test@test.com").user_password("asdf").user_name("홍길동").user_phoneNumber("010-1234-1234").build();

        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        Save save1 = Save.builder().user(testuser).build();

//        given(saveRepository.findByUser_product_id(any(),any())).willReturn(save1);

        Product product = productService.productInfoLogin(1L, 10L);


        Save save = verify(saveRepository, times(1)).findByUser_product_id(
                eq(10L),
                eq(1L)
        );

        assertThat(product.isSaved()).isFalse();


//        assertThat(product.isSaved()).isTrue();




    }
    @Test
    void productSearchTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest1").product_name("test1").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest1").product_name("test1").build();
        Product product4 =Product.builder().product_id(4L).product_manu("testtest1").product_name("test1").build();
        Product product5 =Product.builder().product_id(5L).product_manu("testtest1").product_name("test1").build();
        Product product6 =Product.builder().product_id(6L).product_manu("testtest1").product_name("test1").build();

        List<Product> productAll = new ArrayList<>();
        productAll.add(product1);
        productAll.add(product2);
        productAll.add(product3);
        productAll.add(product4);
        productAll.add(product5);
        productAll.add(product6);

        Pageable pageable = PageRequest.of(0, 6);
        Page<Product> productPage = new PageImpl<>(productAll, pageable, productAll.size());

        Category category1 = Category.builder().category_no(1).product(product1).build();
        Category category2 = Category.builder().category_no(2).product(product2).build();
        Category category3 = Category.builder().category_no(3).product(product3).build();
        Category category4 = Category.builder().category_no(4).product(product4).build();
        Category category5 = Category.builder().category_no(5).product(product5).build();
        Category category6 = Category.builder().category_no(6).product(product6).build();

        given(productRepository.productSearchName(any(),any())).willReturn(productPage);

        given(categoryRepository.findByProduct_id(1L)).willReturn(category1);
        given(categoryRepository.findByProduct_id(2L)).willReturn(category2);
        given(categoryRepository.findByProduct_id(3L)).willReturn(category3);
        given(categoryRepository.findByProduct_id(4L)).willReturn(category4);
        given(categoryRepository.findByProduct_id(5L)).willReturn(category5);
        given(categoryRepository.findByProduct_id(6L)).willReturn(category6);

        Page<Product> products = productService.productSearch("과자", Pageable.ofSize(1));

        assertThat(products.getContent().get(0).getProduct_id()).isEqualTo(1L);
        assertThat(products.getContent().get(0).getCategory()).isEqualTo("IT");
        assertThat(products.getContent().get(2).getProduct_id()).isEqualTo(3L);
        assertThat(products.getContent().get(2).getCategory()).isEqualTo("생활");

    }
    @Test
    void productCategoryTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest1").product_name("test1").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest1").product_name("test1").build();
        Product product4 =Product.builder().product_id(4L).product_manu("testtest1").product_name("test1").build();
        Product product5 =Product.builder().product_id(5L).product_manu("testtest1").product_name("test1").build();
        Product product6 =Product.builder().product_id(6L).product_manu("testtest1").product_name("test1").build();

        List<Product> productAll = new ArrayList<>();
        productAll.add(product1);
        productAll.add(product2);
        productAll.add(product3);
        productAll.add(product4);
        productAll.add(product5);
        productAll.add(product6);

        Pageable pageable = PageRequest.of(0, 8);
        Page<Product> productPage = new PageImpl<>(productAll, pageable, productAll.size());

        Category category1 = Category.builder().category_no(1).product(product1).build();
        Category category2 = Category.builder().category_no(1).product(product2).build();
        Category category3 = Category.builder().category_no(1).product(product3).build();
        Category category4 = Category.builder().category_no(1).product(product4).build();
        Category category5 = Category.builder().category_no(1).product(product5).build();
        Category category6 = Category.builder().category_no(1).product(product6).build();

        given(productRepository.findByCategory(1,Pageable.ofSize(1))).willReturn(productPage);

        given(categoryRepository.findByProduct_id(1L)).willReturn(category1);
        given(categoryRepository.findByProduct_id(2L)).willReturn(category2);
        given(categoryRepository.findByProduct_id(3L)).willReturn(category3);
        given(categoryRepository.findByProduct_id(4L)).willReturn(category4);
        given(categoryRepository.findByProduct_id(5L)).willReturn(category5);
        given(categoryRepository.findByProduct_id(6L)).willReturn(category6);

        Page<Product> products = productService.productCategory("IT", Pageable.ofSize(1));

        assertThat(products.getContent().get(0).getProduct_id()).isEqualTo(1L);
        assertThat(products.getContent().get(0).getCategory()).isEqualTo("IT");
        assertThat(products.getContent().get(2).getProduct_id()).isEqualTo(3L);
        assertThat(products.getContent().get(2).getCategory()).isEqualTo("IT");

    }
    @Test
    void saveInfoTest(){
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest2").product_name("test2").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest3").product_name("test3").build();
        Product product4 =Product.builder().product_id(4L).product_manu("testtest4").product_name("test4").build();
        Product product5 =Product.builder().product_id(5L).product_manu("testtest5").product_name("test5").build();


        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Save save1 = Save.builder().save_id(10L).user(testuser1).product(product1).build();
        Save save2 = Save.builder().save_id(20L).user(testuser1).product(product2).build();
        Save save3 = Save.builder().save_id(30L).user(testuser1).product(product3).build();
        Save save4 = Save.builder().save_id(40L).user(testuser1).product(product4).build();
        Save save5 = Save.builder().save_id(50L).user(testuser1).product(product5).build();

        List<Save> saves = new ArrayList<>();
        saves.add(save1);
        saves.add(save2);
        saves.add(save3);
        saves.add(save4);
        saves.add(save5);

        given(saveRepository.findByUser_id(1L)).willReturn(saves);

        List<Save> saves1 = productService.saveInfo(1L, request);

        assertThat(saves1.get(0).getSave_id()).isEqualTo(10L);
        assertThat(saves1.get(0).getProduct().getProduct_id()).isEqualTo(1L);
        assertThat(saves1.get(0).getUser().getUser_id()).isEqualTo(1L);
        assertThat(saves1.get(1).getSave_id()).isEqualTo(20L);
        assertThat(saves1.get(1).getProduct().getProduct_id()).isEqualTo(2L);
        assertThat(saves1.get(1).getUser().getUser_id()).isEqualTo(1L);
        assertThat(saves1.get(2).getSave_id()).isEqualTo(30L);
        assertThat(saves1.get(2).getProduct().getProduct_id()).isEqualTo(3L);
        assertThat(saves1.get(2).getUser().getUser_id()).isEqualTo(1L);
        assertThat(saves1.get(3).getSave_id()).isEqualTo(40L);
        assertThat(saves1.get(3).getProduct().getProduct_id()).isEqualTo(4L);
        assertThat(saves1.get(3).getUser().getUser_id()).isEqualTo(1L);


    }
    @Test
    void ReviewInfoTest() {
        Product product1 = Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review1 = Review.builder().review_id(10L).user(testuser1).product(product1).build();
        Review review2 = Review.builder().review_id(20L).user(testuser1).product(product1).build();
        Review review3 = Review.builder().review_id(30L).user(testuser1).product(product1).build();
        Review review4 = Review.builder().review_id(40L).user(testuser1).product(product1).build();
        Review review5 = Review.builder().review_id(50L).user(testuser1).product(product1).build();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        reviews.add(review5);

        given(productReviewRepository.findProduct_id(1L)).willReturn(reviews);
        given(reviewLikeRepository.countByReview(10L)).willReturn(20L);
        given(reviewLikeRepository.countByReview(20L)).willReturn(30L);
        given(reviewLikeRepository.countByReview(30L)).willReturn(40L);
        given(reviewLikeRepository.countByReview(40L)).willReturn(50L);
        given(reviewLikeRepository.countByReview(50L)).willReturn(60L);

        List<Review> reviews1 = productService.ReviewInfo(1L, request);

        log.info("리뷰 크기"+String.valueOf(reviews1.size()));

        assertThat(reviews1.get(0).getReview_id()).isEqualTo(10L);
        assertThat(reviews1.get(0).getProduct().getProduct_manu()).isEqualTo("testtest1");
        assertThat(reviews1.get(2).getReview_id()).isEqualTo(30L);
        assertThat(reviews1.get(2).getProduct().getProduct_manu()).isEqualTo("testtest1");
    }
    @Test
    void ReviewAllTest() {
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review1 = Review.builder().review_id(10L).user(testuser1).product(product1).build();
        Review review2 = Review.builder().review_id(20L).user(testuser1).product(product1).build();
        Review review3 = Review.builder().review_id(30L).user(testuser1).product(product1).build();
        Review review4 = Review.builder().review_id(40L).user(testuser1).product(product1).build();
        Review review5 = Review.builder().review_id(50L).user(testuser1).product(product1).build();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        reviews.add(review5);

        given(productReviewRepository.findAll()).willReturn(reviews);
        given(reviewLikeRepository.countByReview(10L)).willReturn(20L);
        given(reviewLikeRepository.countByReview(20L)).willReturn(30L);
        given(reviewLikeRepository.countByReview(30L)).willReturn(40L);
        given(reviewLikeRepository.countByReview(40L)).willReturn(50L);
        given(reviewLikeRepository.countByReview(50L)).willReturn(60L);

        List<Review> reviews1 = productService.ReviewAll();

        assertThat(reviews1.get(0).getReview_id()).isEqualTo(10L);
        assertThat(reviews1.get(0).getProduct().getProduct_manu()).isEqualTo("testtest1");
        assertThat(reviews1.get(2).getReview_id()).isEqualTo(30L);
        assertThat(reviews1.get(2).getProduct().getProduct_manu()).isEqualTo("testtest1");
    }
    @Test
    void ProductAllTest() {
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();
        Product product2 =Product.builder().product_id(2L).product_manu("testtest2").product_name("test2").build();
        Product product3 =Product.builder().product_id(3L).product_manu("testtest3").product_name("test3").build();
        Product product4 =Product.builder().product_id(4L).product_manu("testtest4").product_name("test4").build();
        Product product5 =Product.builder().product_id(5L).product_manu("testtest5").product_name("test5").build();

        Category category1 = Category.builder().category_no(1).product(product1).build();
        Category category2 = Category.builder().category_no(2).product(product2).build();
        Category category3 = Category.builder().category_no(3).product(product3).build();
        Category category4 = Category.builder().category_no(4).product(product4).build();
        Category category5 = Category.builder().category_no(5).product(product5).build();

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);

        given(productRepository.findAll()).willReturn(products);
        given(categoryRepository.findByProduct_id(1L)).willReturn(category1);
        given(categoryRepository.findByProduct_id(2L)).willReturn(category2);
        given(categoryRepository.findByProduct_id(3L)).willReturn(category3);
        given(categoryRepository.findByProduct_id(4L)).willReturn(category4);
        given(categoryRepository.findByProduct_id(5L)).willReturn(category5);

        List<Product> products1 = productService.ProductAll();

        assertThat(products1.get(0).getProduct_id()).isEqualTo(1L);
        assertThat(products1.get(0).getCategory()).isEqualTo("IT");
        assertThat(products1.get(2).getProduct_id()).isEqualTo(3L);
        assertThat(products1.get(2).getCategory()).isEqualTo("생활");
    }
    @Test
    void ReviewInfo_LoginTest() {
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review1 = Review.builder().review_id(10L).user(testuser1).product(product1).build();
        Review review2 = Review.builder().review_id(20L).user(testuser1).product(product1).build();
        Review review3 = Review.builder().review_id(30L).user(testuser1).product(product1).build();
        Review review4 = Review.builder().review_id(40L).user(testuser1).product(product1).build();
        Review review5 = Review.builder().review_id(50L).user(testuser1).product(product1).build();

        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        reviews.add(review5);

        given(productReviewRepository.findProduct_id(1L)).willReturn(reviews);
        given(reviewLikeRepository.countByReview(10L)).willReturn(20L);
        given(reviewLikeRepository.countByReview(20L)).willReturn(30L);
        given(reviewLikeRepository.countByReview(30L)).willReturn(40L);
        given(reviewLikeRepository.countByReview(40L)).willReturn(50L);
        given(reviewLikeRepository.countByReview(50L)).willReturn(60L);

        given(reviewLikeRepository.existsByReviewAndUser(review1,testuser1)).willReturn(true);
        given(reviewLikeRepository.existsByReviewAndUser(review2,testuser1)).willReturn(false);
        given(reviewLikeRepository.existsByReviewAndUser(review3,testuser1)).willReturn(false);
        given(reviewLikeRepository.existsByReviewAndUser(review4,testuser1)).willReturn(true);
        given(reviewLikeRepository.existsByReviewAndUser(review5,testuser1)).willReturn(true);



        List<Review> reviews1 = productService.ReviewInfo_Login(1L,testuser1);

        assertThat(reviews1.get(0).getReview_id()).isEqualTo(10L);
        assertThat(reviews1.get(0).getLikeCount()).isEqualTo(20L);
        assertThat(reviews1.get(0).isLiked()).isEqualTo(true);
        assertThat(reviews1.get(0).getProduct().getProduct_manu()).isEqualTo("testtest1");
        assertThat(reviews1.get(2).getReview_id()).isEqualTo(30L);
        assertThat(reviews1.get(2).getLikeCount()).isEqualTo(40L);
        assertThat(reviews1.get(2).isLiked()).isEqualTo(false);
        assertThat(reviews1.get(2).getProduct().getProduct_manu()).isEqualTo("testtest1");
    }
    @Test
    void RatingCountTest(){
        ReviewRatingCountDto ratingCountDto1 = new ReviewRatingCountDto(1,3L);
        ReviewRatingCountDto ratingCountDto2 = new ReviewRatingCountDto(2,2L);
        ReviewRatingCountDto ratingCountDto3 = new ReviewRatingCountDto(3,1L);
        ReviewRatingCountDto ratingCountDto4 = new ReviewRatingCountDto(4,4L);
        ReviewRatingCountDto ratingCountDto5 = new ReviewRatingCountDto(5,5L);

        List<ReviewRatingCountDto> ratingCountDtos = new ArrayList<>();
        ratingCountDtos.add(ratingCountDto1);
        ratingCountDtos.add(ratingCountDto2);
        ratingCountDtos.add(ratingCountDto3);
        ratingCountDtos.add(ratingCountDto4);
        ratingCountDtos.add(ratingCountDto5);

        given(productReviewRepository.ratingCount(1L)).willReturn(ratingCountDtos);

        Map<Integer, Long> integerLongMap = productService.RatingCount(1L);

        assertThat(integerLongMap.size()).isEqualTo(5);
        assertThat(integerLongMap.get(1)).isEqualTo(3L);
        assertThat(integerLongMap.get(2)).isEqualTo(2L);
        assertThat(integerLongMap.get(3)).isEqualTo(1L);
        assertThat(integerLongMap.get(4)).isEqualTo(4L);
        assertThat(integerLongMap.get(5)).isEqualTo(5L);

        List<ReviewRatingCountDto> ratingCountDtos2 = new ArrayList<>();

        given(productReviewRepository.ratingCount(1L)).willReturn(ratingCountDtos2);

        Map<Integer, Long> integerLongMap2 = productService.RatingCount(1L);

        assertThat(integerLongMap2.size()).isEqualTo(5);
        assertThat(integerLongMap2.get(1)).isEqualTo(0L);
        assertThat(integerLongMap2.get(2)).isEqualTo(0L);
        assertThat(integerLongMap2.get(3)).isEqualTo(0L);
        assertThat(integerLongMap2.get(4)).isEqualTo(0L);
        assertThat(integerLongMap2.get(5)).isEqualTo(0L);

    }

    @Test
    void chartDataTest() {
        Product product1 =Product.builder().product_id(1L).product_manu("testtest1").product_name("test1").build();

        given(productRepository.findById(1L)).willReturn(Optional.of(product1));

        Map<String, Object> stringObjectMap = productService.chartData(1L);

        assertThat(stringObjectMap.get("data")).isEqualTo(List.of(0,0,0,0,0));
        assertThat(stringObjectMap.get("data")).isEqualTo(List.of(0,0,0,0,0));
        assertThat(stringObjectMap.get("labels")).isEqualTo(List.of("총 별점", "가성비 별점", "내구성 별점", "품질 별점", "디자인 별점"));
        assertThat(stringObjectMap.get("total")).isEqualTo(List.of(0L,0L,0L,0L,0L));

    }

    @Test
    void productSaveTest() {
        saveDto saveDto =  new saveDto();
        saveDto.setProduct_id(12L);
        saveDto.setUser_id(1L);

        Product product =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();
        User testuser = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Save save = Save.builder().user(testuser).product(product).build();

        given(saveRepository.findByUser_product_id(1L,12L)).willReturn(save);

        boolean b = productService.productSave(saveDto, request);

//        verify(saveRepository,times(1)).save(eq(save));
        verify(saveRepository,times(1)).delete(eq(save));


        assertThat(b).isTrue();
    }

    @Test
    void reviewCreateTest() {

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setProduct_id(12L);
        reviewCreateDto.setUser_id(1L);
        reviewCreateDto.setCoef_rating(3);
        reviewCreateDto.setDesign_rating(4);
        reviewCreateDto.setDurability_rating(2);
        reviewCreateDto.setQuality_rating(1);
        reviewCreateDto.setContent("testText");

        int sum = reviewCreateDto.getCoef_rating() + reviewCreateDto.getDurability_rating() + reviewCreateDto.getQuality_rating() + reviewCreateDto.getDesign_rating();

        int total = sum / 4;

        MockMultipartFile[] multipartFile  = new MockMultipartFile[1];
        multipartFile[0] = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());

        Product product1 =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();


        Review review = Review.builder()
                .review_id(100L) // 리포지토리 리턴 값이기 때문에 id를 붙여 주는게 좋음
                .user(testuser1)
                .product(product1)
                .build();

        given(productRepository.findById(12L)).willReturn(Optional.of(product1));
        given(userRepository.findById(1L)).willReturn(Optional.of(testuser1));
        given(productReviewRepository.save(any())).willReturn(review);
        given(fileService.imgSave(any(),any(),any())).willReturn("test");


        boolean b = productService.reviewCreate(reviewCreateDto, multipartFile, request);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        ArgumentCaptor<ReviewImg> captorImg = ArgumentCaptor.forClass(ReviewImg.class);

        verify(productReviewRepository,times(1)).save(captor.capture());
        verify(productReviewImgRepository,times(1)).save(captorImg.capture());

        Review value = captor.getValue();
        ReviewImg value1 = captorImg.getValue();

        assertThat(b).isTrue();
        assertThat(value.getCoef_rating()).isEqualTo(3);
        assertThat(value.getDurability_rating()).isEqualTo(2);
        assertThat(value.getQuality_rating()).isEqualTo(1);
        assertThat(value.getDesign_rating()).isEqualTo(4);
        assertThat(value1.getReview_img_name()).isEqualTo("test");

    }
    @Test
    void reviewUpdateTest() {

        ReviewTotalDto reviewTotalDto = new ReviewTotalDto();
        reviewTotalDto.setReviewId(123L);
        reviewTotalDto.setProductId(12L);
        reviewTotalDto.setUserId(1L);
        reviewTotalDto.setUpdateContent("testText");
        reviewTotalDto.setCoefRating(1);
        reviewTotalDto.setDesignRating(2);
        reviewTotalDto.setDurabilityRating(3);
        reviewTotalDto.setQualityRating(1);

        MockMultipartFile[] multipartFile  = new MockMultipartFile[1];
        multipartFile[0] = new MockMultipartFile("file", "test.png", "image/png", "test".getBytes());

        Product product1 =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review = Review.builder()
                .review_id(123L) // 리포지토리 리턴 값이기 때문에 id를 붙여 주는게 좋음
                .user(testuser1)
                .product(product1)
                .build();

        given(productReviewRepository.findById(reviewTotalDto.getReviewId())).willReturn(Optional.of(review));
        given(userRepository.findById(1L)).willReturn(Optional.of(testuser1));
        given(fileService.imgSave(any(),any(),any())).willReturn("test");
        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);

        boolean b = productService.reviewUpdate(reviewTotalDto, multipartFile, request);

        ArgumentCaptor<ReviewImg> captorImg = ArgumentCaptor.forClass(ReviewImg.class);
        verify(productReviewImgRepository,times(1)).save(captorImg.capture());

        assertThat(b).isTrue();

        ReviewImg value1 = captorImg.getValue();
        assertThat(value1.getReview_img_name()).isEqualTo("test");

    }
    @Test
    void reviewDeleteTest() {
        Product product1 =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review = Review.builder()
                .review_id(100L) // 리포지토리 리턴 값이기 때문에 id를 붙여 주는게 좋음
                .user(testuser1)
                .product(product1)
                .build();

        ReviewImg reviewImg1 = ReviewImg.builder().review(review).review_img_id(121L).review_img_name("test1").build();
        ReviewImg reviewImg2 = ReviewImg.builder().review(review).review_img_id(122L).review_img_name("test2").build();
        ReviewImg reviewImg3 = ReviewImg.builder().review(review).review_img_id(123L).review_img_name("test3").build();
        ReviewImg reviewImg4 = ReviewImg.builder().review(review).review_img_id(124L).review_img_name("test4").build();
        ReviewImg reviewImg5 = ReviewImg.builder().review(review).review_img_id(125L).review_img_name("test5").build();

        List<ReviewImg> reviewImgs = new ArrayList<>();
        reviewImgs.add(reviewImg1);
        reviewImgs.add(reviewImg2);
        reviewImgs.add(reviewImg3);
        reviewImgs.add(reviewImg4);
        reviewImgs.add(reviewImg5);

        given(productReviewRepository.findById(100L)).willReturn(Optional.of(review));
        given(productReviewImgRepository.findByReview_id(100L)).willReturn(reviewImgs);
        given(tokenProvider.AuthenticationCheck(request)).willReturn(true);
        given(userRepository.findById(1L)).willReturn(Optional.of(testuser1));


        Long result = productService.reviewDelete(100L, request);

        ArgumentCaptor<String> captorImg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ReviewImg> captorReviewImg = ArgumentCaptor.forClass(ReviewImg.class);

        verify(fileService,times(5)).imgDelete(eq("review"),captorImg.capture());
        verify(productReviewImgRepository,times(5)).delete(captorReviewImg.capture());
        verify(productReviewRepository,times(1)).delete(eq(review));

        assertThat(result).isEqualTo(12L);
        assertThat(captorReviewImg.getValue().getReview_img_name()).isEqualTo("test5");
        assertThat(captorImg.getValue()).isEqualTo("test5");
    }

    @Test
    @DisplayName("삭제 테스트")
    void reviewLikeTrueTest() {
        Product product1 =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review = Review.builder()
                .review_id(100L) // 리포지토리 리턴 값이기 때문에 id를 붙여 주는게 좋음
                .user(testuser1)
                .product(product1)
                .build();

        ReviewLikeDto reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setReview(review);
        reviewLikeDto.setReview_ch(1234);
        reviewLikeDto.setUser(testuser1);

        ReviewLike reviewLike = ReviewLike.builder().review_like_id(4321L).review(review).review_ch(1234).user(testuser1).build();

        given(reviewLikeRepository.existsByReviewAndUser(review,testuser1)).willReturn(true, false);
        given(reviewLikeRepository.findByUserIdAndReviewId(1L,100L)).willReturn(reviewLike);
        given(reviewLikeRepository.countByReview(100L)).willReturn(12L);
//        given(reviewLikeRepository.existsByReviewAndUser(review,testuser1)).willReturn(false);

//        given(reviewLikeRepository.existsByReviewAndUser(null,null)).willReturn(false);

        Review reviewResult = productService.reviewLike(reviewLikeDto, request);

        verify(reviewLikeRepository,times(1)).delete(eq(reviewLike));

        assertThat(reviewResult.getReview_id()).isEqualTo(100L);
        assertThat(reviewResult.isLiked()).isFalse();
    }

    @Test
    @DisplayName("저장 테스트")
    void reviewLikeFalseTest() {
        Product product1 =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        User testuser1 = User.builder().user_id(1L).userEmail("test1@test.com").user_password("asdf1").user_name("홍길동1").user_phoneNumber("010-1234-1234").build();

        Review review = Review.builder()
                .review_id(100L) // 리포지토리 리턴 값이기 때문에 id를 붙여 주는게 좋음
                .user(testuser1)
                .product(product1)
                .build();

        ReviewLikeDto reviewLikeDto = new ReviewLikeDto();
        reviewLikeDto.setReview(review);
        reviewLikeDto.setReview_ch(1234);
        reviewLikeDto.setUser(testuser1);


        given(reviewLikeRepository.existsByReviewAndUser(review,testuser1)).willReturn(false, true);
        given(reviewLikeRepository.countByReview(100L)).willReturn(13L);
//        given(reviewLikeRepository.existsByReviewAndUser(review,testuser1)).willReturn(false);

//        given(reviewLikeRepository.existsByReviewAndUser(null,null)).willReturn(false);

        Review reviewResult = productService.reviewLike(reviewLikeDto, request);

        verify(reviewLikeRepository,times(1)).save(any(ReviewLike.class));

        assertThat(reviewResult.getReview_id()).isEqualTo(100L);
        assertThat(reviewResult.isLiked()).isTrue();
    }

}