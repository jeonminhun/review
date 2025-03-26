package com.project.review.search.controller;

import com.project.review.category.dto.categoryReviewDto;
import com.project.review.category.service.categoryService;
import com.project.review.product.entity.Product;
import com.project.review.product.service.productService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class searchController {

    private final productService productService;
    private final categoryService categoryService;

    @GetMapping("/search") // 검색 기능
    public String search(@RequestParam("query") String query, @RequestParam("menu")Integer menu, Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        List<categoryReviewDto> categoryReviews = categoryService.categoryReviewCount();
        model.addAttribute("categoryReviews", categoryReviews);

        if (menu == 1) {
            List<Product> products = productService.productSearch(query);
            model.addAttribute("products", products);
        }if (menu == 2) {
            List<Product> products = productService.productCategory(query);
            model.addAttribute("products", products);
        }
        if (authCookie != null) {
            return "after/search";
        }
        return "default/search";
    }
}
