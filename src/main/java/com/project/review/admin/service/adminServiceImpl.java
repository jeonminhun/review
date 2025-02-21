package com.project.review.admin.service;

import com.project.review.admin.dto.UserGradeDto;
import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.productAdminDto;
import com.project.review.product.dto.productImgDto;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.ProductImg;
import com.project.review.product.repository.productImgRepository;
import com.project.review.product.repository.productRepository;
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

@Slf4j
@Service
@AllArgsConstructor
public class adminServiceImpl implements adminService { // adminServiceImpl 트루 폴스 고치기
    private final productRepository productRepository;
    private final productImgRepository productImgRepository;
    private final userRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public boolean checkAdmin(HttpServletRequest request) {
        log.info("관리자 확인 요청입니다.");
        boolean result = tokenProvider.AuthenticationCheck(request);
        return result;
    }

    @Override
    public boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request) {
        try {
            log.info("프로덕트 생성 서비스 시작");
            Product product = Product.builder()
                    .product_name(productCreateDto.getProduct_name())
                    .product_manu(productCreateDto.getProduct_manu()).build();

            String filename = System.currentTimeMillis() + "_" + product.getProduct_name() + ".jpg";

            product =  productRepository.save(product);

            ProductImg productImg = imgSave(files, request, product, filename);

            productImgRepository.save(productImg);

            return true;

        } catch (Exception e) {
            log.info("제품 생성 실패 : " + productCreateDto.getProduct_name());
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean productDelete(productAdminDto productAdminDto) {

        try {
            log.info("프로덕트 삭제 서비스 시작");

            Product product = Product.builder()
                    .product_id(productAdminDto.getProductDto().getProduct_id())
                    .product_name(productAdminDto.getProductDto().getProduct_name())
                    .product_manu(productAdminDto.getProductDto().getProduct_manu())
                    .product_coef_rating(productAdminDto.getProductDto().getProduct_coef_rating())
                    .product_durability_rating(productAdminDto.getProductDto().getProduct_durability_rating())
                    .product_quality_rating(productAdminDto.getProductDto().getProduct_quality_rating())
                    .product_design_rating(productAdminDto.getProductDto().getProduct_design_rating())
                    .product_total_rating(productAdminDto.getProductDto().getProduct_total_rating())
                    .build();

            ProductImg productImg = ProductImg.builder()
                    .product_img_id(productAdminDto.getProductImgDto().getProduct_img_id())
                    .product(productAdminDto.getProductImgDto().getProduct())
                    .product_img_name(productAdminDto.getProductImgDto().getProduct_img_name()).build();

            productImgRepository.delete(productImg);
            productRepository.delete(product);

            imgDelete(productAdminDto.getProductImgDto());

            return true;


        } catch (Exception e) {
            log.info("프로덕트 삭제 실패 : "+productAdminDto.getProductDto().getProduct_name());
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean productUpdate(productAdminDto productAdminDto, MultipartFile files, HttpServletRequest request) {
        try {
            log.info("프로덕트 수정 서비스 시작");
            Product product = Product.builder()
                    .product_id(productAdminDto.getProductDto().getProduct_id())
                    .product_name(productAdminDto.getProductDto().getProduct_name())
                    .product_manu(productAdminDto.getProductDto().getProduct_manu())
                    .product_coef_rating(productAdminDto.getProductDto().getProduct_coef_rating())
                    .product_durability_rating(productAdminDto.getProductDto().getProduct_durability_rating())
                    .product_quality_rating(productAdminDto.getProductDto().getProduct_quality_rating())
                    .product_design_rating(productAdminDto.getProductDto().getProduct_design_rating())
                    .product_total_rating(productAdminDto.getProductDto().getProduct_total_rating())
                    .build();

            ProductImg productImg = ProductImg.builder()
                    .product_img_id(productAdminDto.getProductImgDto().getProduct_img_id())
                    .product(productAdminDto.getProductImgDto().getProduct())
                    .product_img_name(productAdminDto.getProductImgDto().getProduct_img_name())
                    .build();

            String filename = productAdminDto.getProductImgDto().getProduct_img_name();

            product =  productRepository.save(product);

            imgSave(files, request, product, filename);

            productImgRepository.save(productImg);

            return true;
        } catch (Exception e) {
            log.info("프로덕트 수정 실패 : "+ productAdminDto.getProductDto().getProduct_name());
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean userGradeUpdate(UserGradeDto userGradeDto, HttpServletRequest request) {

        try {
            log.info("유저 등급 변경 서비스 시작");
            userRepository.updateUserRole(userGradeDto.getUser_id(),userGradeDto.getChange_role());
            return true;
        } catch (Exception e) {
            log.info("유저 등급 변경 실패 : " + userGradeDto.getUser_name());
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean userDelete(Long user_id, HttpServletRequest request) {
        try {
            log.info("유저 삭제 서비스 시작");
            userRepository.deleteById(user_id);
            return true;
        } catch (Exception e) {
            log.info("유저 삭제 실패 : " + user_id);
            e.printStackTrace();
            return false;
        }
    }

    private void imgDelete(productImgDto productImgDto) {
        try {
            Path uploadPath = Path.of("imgs", "product");
            Path filepath = uploadPath.resolve(productImgDto.getProduct_img_name());
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
        }
    }


    private ProductImg imgSave(MultipartFile files, HttpServletRequest request, Product product, String filename) {
        try {
            if (files != null) {

                Path uploadPath = Path.of("imgs", "product");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                log.info(filename);

                Path filepath = uploadPath.resolve(filename);
                Files.copy(files.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING /*같은 파일이름 있으면 덮어쓰기*/);
                ProductImg productImg = ProductImg.builder()
                        .product(product)
                        .product_img_name(filename)
                        .build();

                return productImg;

            }
            return null;

        } catch (Exception e) {
            log.info("이미지 저장 오류");
            e.printStackTrace();
            return null;
        }
    }
}
