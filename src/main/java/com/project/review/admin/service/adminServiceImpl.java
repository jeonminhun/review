package com.project.review.admin.service;

import com.project.review.admin.dto.UserGradeDto;
import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.adminProductUpdateDto;
import com.project.review.category.dto.categoryEnum;
import com.project.review.category.entity.Category;
import com.project.review.category.repository.categoryRepository;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.ProductImg;
import com.project.review.product.repository.productImgRepository;
import com.project.review.product.repository.productRepository;
import com.project.review.user.jwt.TokenProvider;
import com.project.review.user.repository.userRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@AllArgsConstructor
public class adminServiceImpl implements adminService {
    private final productRepository productRepository;
    private final productImgRepository productImgRepository;
    private final categoryRepository categoryRepository;
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

            product =  productRepository.save(product);

            //제품 이미지 저장
            String filename = "product_" + product.getProduct_id() + ".jpg";

            ProductImg productImg = imgSave(files, request, product, filename);

            productImgRepository.save(productImg);

            // 제품 카테고리 등록
            int codeByCategory = categoryEnum.getCodeByCategory(productCreateDto.getCategory_no());

            Category category = Category.builder().product(product).category_no(codeByCategory).build();

            categoryRepository.save(category);

            return true;

        } catch (Exception e) {
            log.info("제품 생성 실패 : " + productCreateDto.getProduct_name());
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean productDelete(Long product_id) {

        try {
            log.info("프로덕트 삭제 서비스 시작");

            Product product = productRepository.findById(product_id).get();
            ProductImg productImg = productImgRepository.findByProduct_id(product_id);

            productImgRepository.delete(productImg);
            productRepository.delete(product);

            imgDelete(productImg);

            return true;
        } catch (Exception e) {
            log.info("프로덕트 삭제 실패 : "+ product_id);
            e.printStackTrace();
            return false;
        }


    }

    @Override
    @Transactional
    public boolean productUpdate(adminProductUpdateDto adminProductUpdateDto, MultipartFile files, HttpServletRequest request) {
        try {
            log.info("프로덕트 수정 서비스 시작");

            Product product = productRepository.findById(adminProductUpdateDto.getProduct_id()).orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다: " + adminProductUpdateDto.getProduct_id()));

            product.updateProduct(
                    adminProductUpdateDto.getProduct_name(),
                    adminProductUpdateDto.getProduct_manu(),
                    adminProductUpdateDto.getProduct_coef_rating(),
                    adminProductUpdateDto.getProduct_durability_rating(),
                    adminProductUpdateDto.getProduct_quality_rating(),
                    adminProductUpdateDto.getProduct_design_rating(),
                    adminProductUpdateDto.getProduct_total_rating()
            );
            // 이미지 업데이트
            if (!files.isEmpty()) {
                String filename = "product_" + product.getProduct_id() + ".jpg";

                ProductImg productImg = imgSave(files, request, product, filename);

                if (productImgRepository.findByProduct_id(product.getProduct_id()) == null) {
                    productImgRepository.save(productImg);
                }
            }

            // 카테고리 업데이트
            Category category = categoryRepository.findByProduct_id(product.getProduct_id());

            int codeByCategory = categoryEnum.getCodeByCategory(adminProductUpdateDto.getCategory());

            category.updateCategory(codeByCategory);

            return true;
        } catch (Exception e) {
            log.info("프로덕트 수정 실패 : "+ adminProductUpdateDto.getProduct_name());
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

    private void imgDelete(ProductImg productImg) {
        try {
            Path uploadPath = Path.of("src","main","resources","static", "assets", "images", "product");
            Path filepath = uploadPath.resolve(productImg.getProduct_img_name());
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
        }
    }


    private ProductImg imgSave(MultipartFile files, HttpServletRequest request, Product product, String filename) {
        try {
            if (files != null) {

                Path uploadPath = Path.of("src","main","resources","static", "assets", "images", "product");

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
