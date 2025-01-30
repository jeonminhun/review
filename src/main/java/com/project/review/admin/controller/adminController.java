package com.project.review.admin.controller;

import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.service.adminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@AllArgsConstructor
public class adminController { // 사진 받는것도 추가 해야함
    private final adminService adminService;

    @PostMapping("/save")
    public String productInsert(
            @RequestPart(name = "productCreateDto") productCreateDto productCreateDto,
            @RequestPart(required = false, name = "files") MultipartFile files, HttpServletRequest request
    ) {
        log.info("작동은 시작");
        adminService.productCreate(productCreateDto,files, request);
        return "redirect:/";
    }
}
