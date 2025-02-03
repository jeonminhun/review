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

    @Override
    public boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request) {

        Product product = Product.builder()
                .product_name(productCreateDto.getProduct_name())
                .product_manu(productCreateDto.getProduct_manu()).build();

        String filename = System.currentTimeMillis() + "_" + product.getProduct_name() + ".jpg";

        product =  productRepository.save(product);

        ProductImg productImg = imgSave(files, request, product, filename);

        productImgRepository.save(productImg);

        return true;
    }

    @Override
    public boolean productDelete(productAdminDto productAdminDto) {

        log.info("프로덕트 삭제 서비스 시작");

        Product product = Product.builder()
                .product_id(productAdminDto.getProductDto().getProduct_id())
                .product_name(productAdminDto.getProductDto().getProduct_name())
                .product_manu(productAdminDto.getProductDto().getProduct_manu())
                .product_rating(productAdminDto.getProductDto().getProduct_rating())
                .build();

        ProductImg productImg = ProductImg.builder()
                .product_img_id(productAdminDto.getProductImgDto().getProduct_img_id())
                .product(productAdminDto.getProductImgDto().getProduct())
                .product_img_name(productAdminDto.getProductImgDto().getProduct_img_name()).build();

        productImgRepository.delete(productImg);
        productRepository.delete(product);

        imgDelete(productAdminDto.getProductImgDto());

        return true;
    }

    @Override
    public boolean productUpdate(productAdminDto productAdminDto, MultipartFile files, HttpServletRequest request) {

        Product product = Product.builder()
                .product_id(productAdminDto.getProductDto().getProduct_id())
                .product_name(productAdminDto.getProductDto().getProduct_name())
                .product_manu(productAdminDto.getProductDto().getProduct_manu())
                .product_rating(productAdminDto.getProductDto().getProduct_rating())
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
    }

    @Override
    public boolean userGradeUpdate(UserGradeDto userGradeDto, HttpServletRequest request) {
        log.info("뭐지 : " + userGradeDto.getChange_role());
        userRepository.updateUserRole(userGradeDto.getUser_id(),userGradeDto.getChange_role());
        return true;
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
