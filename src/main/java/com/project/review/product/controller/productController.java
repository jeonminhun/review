package com.project.review.product.controller;

import com.project.review.product.dto.ReviewCreateDto;
import com.project.review.product.dto.ReviewLikeDto;
import com.project.review.product.dto.reviewTotalDto;
import com.project.review.product.entity.Product;
import com.project.review.product.entity.ProductImg;
import com.project.review.product.entity.Review;
import com.project.review.product.entity.ReviewImg;
import com.project.review.product.service.productService;
import com.project.review.user.entity.User;
import com.project.review.user.service.userService;
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

        List<Review> reviews = productService.ReviewInfo(product_id, request);
        model.addAttribute("reviews", reviews);

        List<ReviewImg> reviewImgs = productService.ReviewImgInfo(product_id);
        model.addAttribute("reviewImgs", reviewImgs);

        Map<Integer, Long> ratingCount = productService.RatingCount(product_id);
        model.addAttribute("ratingCount", ratingCount);
        model.addAttribute("product_id",product_id);

        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {

            Long user_id = userService.getUserId(request);
            model.addAttribute("user_id", user_id);

            long endTime = System.currentTimeMillis();
            log.info("productPage 실행 시간: {} ms", (endTime - startTime));
            return "after/product-details";
        }
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
    @GetMapping("/chart-data/{product_id}")
    public Map<String, Object> getChartData(@PathVariable("product_id") Long product_id) {
        log.info("차트 데이터 호출 : " + product_id);

        Map<String, Object> chartData = productService.chartData(product_id);

        return chartData; // JSON 형식으로 반환
    }


}
