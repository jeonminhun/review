package com.project.review.product.controller;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.dto.saveDto;
import com.project.review.product.entity.*;
import com.project.review.product.service.productService;
import com.project.review.user.entity.User;
import com.project.review.user.service.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class productController {
    private final productService productService;
    private final userService userService;

    @GetMapping("/product/{product_id}")
    public String productPage(
            @PathVariable("product_id") Long product_id,
            Model model,
            HttpServletRequest request
    ){
        long startTime = System.currentTimeMillis();
        Product product = productService.productInfo(product_id, request);
        model.addAttribute("product", product);

        ProductImg productImg = productService.productImgInfo(product_id);
        model.addAttribute("productImg", productImg);

        List<ReviewImg> reviewImgs = productService.ReviewImgInfo(product_id);
        model.addAttribute("reviewImgs", reviewImgs);

        Map<Integer, Long> ratingCount = productService.RatingCount(product_id);
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("product_id",product_id);

        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {

            Long user_id = userService.getUserId(request);

            Product product_login = productService.productInfoLogin(product_id, user_id);
            model.addAttribute("product", product_login);

            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);

            List<Review> reviews = productService.ReviewInfo_Login(product_id, user);
            model.addAttribute("reviews", reviews);

            long endTime = System.currentTimeMillis();
            log.info("productPage 실행 시간: {} ms", (endTime - startTime));
            return "after/product-details";
        }

        List<Review> reviews = productService.ReviewInfo(product_id, request);
        model.addAttribute("reviews", reviews);

        long endTime = System.currentTimeMillis();
        log.info("productPage 실행 시간: {} ms", (endTime - startTime));
        return "default/product-details";
    }

    @PostMapping("/review")
    public String reviewCreate(
            @ModelAttribute ReviewCreateDto reviewCreateDto,
            @RequestPart("files") MultipartFile[] files,
            HttpServletRequest request)
    {
        log.info("리뷰 생성 시작 : "+reviewCreateDto.getUser_id());
        if (productService.reviewCreate(reviewCreateDto, files, request)) {

            String referer = request.getHeader("Referer");
            return "redirect:"+referer;
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/reviewUpdate")
    public String reviewUpdate(
            @ModelAttribute reviewTotalDto reviewTotalDto,
            @RequestPart("files") MultipartFile[] files,
            HttpServletRequest request)
    {
        if (productService.reviewUpdate(reviewTotalDto, files, request)) {
            String referer = request.getHeader("Referer");
            return "redirect:"+referer;
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/reviewDelete/{review_id}")
    public String reviewDelete(
            @PathVariable("review_id") Long review_id,
            HttpServletRequest request)
    {

        Long product_id = productService.reviewDelete(review_id, request);
        if (product_id != null) {
            log.info("리뷰 삭제성공 : " + product_id);
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        } else {
            log.info("리뷰 삭제 실패");
            return "redirect:/";
        }
    }
    @ResponseBody
    @PostMapping("/reviewLike")
    public ResponseEntity<Object> reviewLike(
            @RequestBody ReviewLikeDto reviewLikeDto,
            HttpServletRequest request)
    {
        Review review = productService.reviewLike(reviewLikeDto, request);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.badRequest().body("실패");
        }
    }

    @ResponseBody
    @PostMapping("/productSave")
    public ResponseEntity<Object> productSave(
            @RequestBody saveDto saveDto,
            HttpServletRequest request)
    {
        boolean save = productService.productSave(saveDto, request);
        if (save) {
            return ResponseEntity.ok("성공");
        } else {
            return ResponseEntity.badRequest().body("실패");
        }
    }


    @ResponseBody
    @GetMapping("/chart-data/{product_id}")
    public Map<String, Object> getChartData(@PathVariable("product_id") Long product_id) {
        log.info("차트 데이터 호출 : " + product_id);

        Map<String, Object> chartData = productService.chartData(product_id);

        return chartData; // JSON 형식으로 반환
    }


}
