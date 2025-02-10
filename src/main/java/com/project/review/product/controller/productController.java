package com.project.review.product.controller;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.Review;
import com.project.review.product.service.productService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/product/{product_id}")
    public String productPage(
            @PathVariable("product_id") Long product_id,
            Model model,
            HttpServletRequest request
    ){
        Product product = productService.productInfo(product_id, request);
        model.addAttribute("product", product);

        List<Review> reviews = productService.ReviewInfo(product_id, request);
        model.addAttribute("reviews", reviews);

        Map<Integer, Long> ratingCount = productService.RatingCount(product_id);
        model.addAttribute("ratingCount", ratingCount);

        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            return "after/product-details";
        }
        return "default/product-details";
    }

    @PostMapping("/review")
    public String reviewCreate(
            @RequestPart("reviewCreateDto") ReviewCreateDto reviewCreateDto,
            @RequestPart("files") MultipartFile[] files,
            HttpServletRequest request)
    {
        if (productService.reviewCreate(reviewCreateDto, files, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/reviewUpdate")
    public String reviewUpdate(
            @RequestPart("reviewUpdateDto") reviewTotalDto reviewTotalDto,
            @RequestPart("files") MultipartFile[] files,
            HttpServletRequest request)
    {
        if (productService.reviewUpdate(reviewTotalDto, files, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/reviewDelete")
    public String reviewDelete(
            @RequestBody reviewTotalDto reviewTotalDto,
            HttpServletRequest request)
    {
        if (productService.reviewDelete(reviewTotalDto, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }



    @PostMapping("/reviewLike")
    public String reviewLike(
            @RequestBody ReviewLikeDto reviewLikeDto,
            HttpServletRequest request)
    {
        if (productService.reviewLike(reviewLikeDto, request)) {
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }



    @ResponseBody
    @GetMapping("/chart-data")
    public Map<String, Object> getChartData() {
        log.info("차트 데이터 호출");

        Map<String, Object> chartData = new HashMap<>();

        // 데이터와 레이블을 준비
        chartData.put("data", Arrays.asList(12, 19, 30, 40, 60)); // 차트에 들어갈 데이터
        chartData.put("labels", Arrays.asList("총 별점", "가성비 별점", "품질 별점", "내구성 별점", "디자인 별점")); // X축 레이블
        chartData.put("total", Arrays.asList(5, 10, 10, 20, 25, 30)); // 차트에 들어갈 데이터

        return chartData; // JSON 형식으로 반환
    }

}
