package com.project.review.admin.service;

import com.project.review.admin.dto.productCreateDto;
import com.project.review.product.entity.product;
import com.project.review.product.entity.ProductImg;
import com.project.review.product.repository.productImgRepository;
import com.project.review.product.repository.productRepository;
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
public class adminServiceImpl implements adminService{
    private final productRepository productRepository;
    private final productImgRepository productImgRepository;
    @Override
    public boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request) {

        product product1 = product.builder()
                .product_name(productCreateDto.getProduct_name())
                .product_manu(productCreateDto.getProduct_manu()).build();

        imgSave(files, request, productRepository.save(product1));

        return true;
    }

    private boolean imgSave(MultipartFile files, HttpServletRequest request, product product) {
        try {
            if (files != null) {

                Path uploadPath = Path.of("imgs","product");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                if (!files.isEmpty()) {
                    String filename = System.currentTimeMillis()+"_"+product.getProduct_name()+".jpg";
                    log.info(filename);
                    Path filepath = uploadPath.resolve(filename);
                    Files.copy(files.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING /*같은 파일이름 있으면 덮어쓰기*/);
                    ProductImg productImg = ProductImg.builder()
                            .product(product)
                            .product_img_name(filename)
                            .build();

                    productImgRepository.save(productImg);
                    }
            }
            return true;

        } catch (Exception e) {
            log.info("이미지 저장 오류");
            e.printStackTrace();
            return false;
        }
    }
}
