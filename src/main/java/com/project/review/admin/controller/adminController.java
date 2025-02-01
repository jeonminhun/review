package com.project.review.admin.controller;

import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.productAdminDto;
import com.project.review.admin.service.adminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@AllArgsConstructor
public class adminController { // 사진 받는것도 추가 해야함
    private final adminService adminService;

    @PostMapping("/save")
    public String productInsert(
            @RequestPart(name = "productCreateDto") productCreateDto productCreateDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("productInsert 호출 : "+ productCreateDto.getProduct_name());
        adminService.productCreate(productCreateDto,files, request);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String productDelete(@RequestBody productAdminDto productAdminDto) {
        log.info("제품 삭제 : " + productAdminDto.getProductDto().getProduct_name());
        adminService.productDelete(productAdminDto);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String productUpdate(
            @RequestPart(name = "productAdminDto") productAdminDto productAdminDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("제품 수정 : " + productAdminDto.getProductDto().getProduct_name());
        adminService.productUpdate(productAdminDto,files,request);
        return "redirect:/";
    }
}
