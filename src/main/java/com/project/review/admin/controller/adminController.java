package com.project.review.admin.controller;

import com.project.review.admin.dto.UserGradeDto;
import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.adminProductUpdateDto;
import com.project.review.admin.service.adminService;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.ProductImg;
import com.project.review.product.entity.Review;
import com.project.review.product.service.productService;
import com.project.review.user.entity.User;
import com.project.review.user.service.userService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class adminController { // 사진 받는것도 추가 해야함
    private final adminService adminService;
    private final productService productService;
    private final userService userService;

    @GetMapping("/adminUser")
    public String adminUser(Model model, HttpServletRequest request) {
        log.info("관리자 페이지 접근 입니다.");
        if (adminService.checkAdmin(request)) {
            log.info("관리자 확인했습니다.");
            List<User> Users = userService.findAllUser();
            List<Review> reviews = productService.ReviewAll();
            List<Product> Product = productService.ProductAll();
            List<ProductImg> productImgs = productService.ProductImgAll();
            log.info("로그 확인용 입니다. : "+Users.get(0).getUser_name());
            model.addAttribute("Users", Users);
            model.addAttribute("reviews", reviews);
            model.addAttribute("Product", Product);
            model.addAttribute("productImgs", productImgs);
            return "after/adminPage";
        }
        return "default/index";


    }

    @PostMapping("/product/save")
    public String productInsert(
            @ModelAttribute(name = "productCreateDto") productCreateDto productCreateDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("제품 생성 : " + productCreateDto.getProduct_name());
        if (adminService.productCreate(productCreateDto, files, request))
        {
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        }
        else {
            return "redirect:/";
        }
    }

    @PostMapping("/product/delete/{product_id}")
    public String productDelete(@PathVariable("product_id") Long product_id,
                                HttpServletRequest request) {
        log.info("제품 삭제 : " + product_id);
        if (adminService.productDelete(product_id)) {
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/product/update")
    public String productUpdate(
            @ModelAttribute adminProductUpdateDto adminProductUpdateDto,
            @RequestPart(required = false, name = "files") MultipartFile files,
            HttpServletRequest request
    ) {
        log.info("제품 수정 : " + adminProductUpdateDto.getProduct_name());
        if (adminService.productUpdate(adminProductUpdateDto, files, request)) {
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
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
            return "redirect:/admin/adminUser";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/user/delete/{user_id}")
    public String userDelete( // adminServiceImpl 트루 폴스 고치기
            @PathVariable("user_id") Long user_id,
            HttpServletRequest request
    ) {
        log.info("유저 삭제 : " + user_id);
        if (adminService.userDelete(user_id, request)) {
            return "redirect:/admin/adminUser";
        } else {
            return "redirect:/";
        }
    }
}

