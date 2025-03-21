package com.project.review.search.controller;

import com.project.review.product.entity.Product;
import com.project.review.product.service.productService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class searchController {

    private final productService productService;

    @GetMapping("/search") // 페이지 확인용 임시 컨트롤러
    public String search(Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            List<Product> products = productService.productAll();
            model.addAttribute("products", products);
            return "after/search";
        }
        return "default/search";
    }



}
