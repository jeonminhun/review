package com.project.review.admin.controller;

import com.project.review.admin.dto.UserGradeDto;
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
@RequestMapping("/admin")
public class adminController { // 사진 받는것도 추가 해야함
    private final adminService adminService;

    @PostMapping("/product/save")
    public String productInsert(
            @RequestPart(name = "productCreateDto") productCreateDto productCreateDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("제품 생성 : " + productCreateDto.getProduct_name());
        if (adminService.productCreate(productCreateDto, files, request))
        {
            return "redirect:/";
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("/product/delete")
    public String productDelete(@RequestBody productAdminDto productAdminDto) {
        log.info("제품 삭제 : " + productAdminDto.getProductDto().getProduct_name());
        if (adminService.productDelete(productAdminDto)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }



    }

    @PostMapping("/product/update")
    public String productUpdate(
            @RequestPart(name = "productAdminDto") productAdminDto productAdminDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("제품 수정 : " + productAdminDto.getProductDto().getProduct_name());
        if (adminService.productUpdate(productAdminDto, files, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }



    }

    @PostMapping("/user/grade")
    public String userGrade( // adminServiceImpl 트루 폴스 고치기
            @RequestBody UserGradeDto userGradeDto,
            HttpServletRequest request
    ) {
        log.info("유저 설정 : " + userGradeDto.getUser_name());
        if (adminService.userGradeUpdate(userGradeDto, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }



    }


}

