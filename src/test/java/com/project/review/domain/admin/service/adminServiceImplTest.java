package com.project.review.domain.admin.service;

import com.project.review.domain.admin.dto.UserGradeDto;
import com.project.review.domain.admin.dto.productCreateDto;
import com.project.review.domain.admin.entity.adminProductUpdateDto;
import com.project.review.domain.category.entity.Category;
import com.project.review.domain.category.repository.categoryRepository;
import com.project.review.domain.product.entity.Product;
import com.project.review.domain.product.entity.ProductImg;
import com.project.review.domain.product.repository.*;
import com.project.review.domain.product.service.productServiceImpl;
import com.project.review.domain.user.jwt.TokenProvider;
import com.project.review.domain.user.repository.userRepository;
import com.project.review.global.util.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import static org.junit.jupiter.api.Assertions.*;




@Slf4j
@ExtendWith(MockitoExtension.class)
class adminServiceImplTest {

    @InjectMocks
    private adminServiceImpl adminService;
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
    void productCreate() {
        productCreateDto productCreateDto = new productCreateDto();
        productCreateDto.setProduct_name("Test Product");
        productCreateDto.setProduct_manu("Test Manu");
        productCreateDto.setCategory_no("IT");

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", "Test".getBytes());

        Product product =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();


        given(productRepository.save(any())).willReturn(product);
        given(fileService.imgSave(multipartFile,"product","product_12.jpg")).willReturn("product_12.jpg");

        boolean b = adminService.productCreate(productCreateDto, multipartFile, request);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(productImgRepository, times(1)).save(any(ProductImg.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(fileService, times(1)).imgSave(eq(multipartFile), eq("product"), eq("product_12.jpg"));

        assertThat(b).isTrue();
    }

    @Test
    void productDeleteTest() {
        Product product =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();
        ProductImg productImg = ProductImg.builder().product(product).product_img_id(43L).product_img_name("testImg").build();

        given(productRepository.findById(12L)).willReturn(Optional.of(product));
        given(productImgRepository.findByProduct_id(12L)).willReturn(productImg);

        boolean result = adminService.productDelete(12L);

        ArgumentCaptor<ProductImg> captor = ArgumentCaptor.forClass(ProductImg.class);
        ArgumentCaptor<Product> captorProduct = ArgumentCaptor.forClass(Product.class);

        verify(productRepository, times(1)).delete(captorProduct.capture());
        verify(productImgRepository, times(1)).delete(captor.capture());
        verify(fileService, times(1)).imgDelete(eq("product"), eq("testImg"));

        Product value = captorProduct.getValue();
        ProductImg ImgValue = captor.getValue();

        assertThat(result).isTrue();

        assertThat(value.getProduct_id()).isEqualTo(product.getProduct_id());
        assertThat(ImgValue.getProduct_img_id()).isEqualTo(productImg.getProduct_img_id());
    }
    @Test
    void productUpdateTest() {

        Product product =Product.builder().product_id(12L).product_manu("testtest1").product_name("test1").build();

        ProductImg productImg = ProductImg.builder().product(product).product_img_id(43L).product_img_name("testImg").build();

        Category category = Category.builder().category_no(1).product(product).build();


        adminProductUpdateDto adminProductUpdateDto = new adminProductUpdateDto();
        adminProductUpdateDto.setProduct_id(12L);
        adminProductUpdateDto.setProduct_name("Test Product");
        adminProductUpdateDto.setProduct_manu("Test Manu");
        adminProductUpdateDto.setProduct_coef_rating(5);
        adminProductUpdateDto.setProduct_durability_rating(4);
        adminProductUpdateDto.setProduct_design_rating(3);
        adminProductUpdateDto.setProduct_quality_rating(2);
        adminProductUpdateDto.setProduct_total_rating(3);
        adminProductUpdateDto.setCategory("IT");

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png", "image/png", "Test".getBytes());

        given(productRepository.findById(12L)).willReturn(Optional.of(product));
        given(fileService.imgSave(multipartFile,"product","product_12.jpg")).willReturn("product_12.jpg");
        given(productImgRepository.findByProduct_id(12L)).willReturn(productImg);
        given(categoryRepository.findByProduct_id(12L)).willReturn(category);


        boolean b = adminService.productUpdate(adminProductUpdateDto, multipartFile, request);

        assertThat(b).isTrue();
    }

    @Test
    void userGradeUpdateTest() {

        UserGradeDto userGradeDto = new UserGradeDto();
        userGradeDto.setUser_id(12L);
        userGradeDto.setUser_name("Test User");
        userGradeDto.setChange_role(3);

        boolean result = adminService.userGradeUpdate(userGradeDto, request);

        verify(userRepository,times(1)).updateUserRole(eq(userGradeDto.getUser_id()),eq(userGradeDto.getChange_role()));

        assertThat(result).isTrue();

    }
    @Test
    void userDeleteTest() {
        boolean result = adminService.userDelete(1L, request);

        verify(userRepository,times(1)).deleteById(eq(1L));

        assertThat(result).isTrue();
    }
}