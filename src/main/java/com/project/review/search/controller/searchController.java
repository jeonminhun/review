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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class searchController {

    private final productService productService;

   /* @GetMapping("/search") // 페이지 확인용 임시 컨트롤러
    public String search(Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        List<Product> products = productService.productAll();
        model.addAttribute("products", products);

        if (authCookie != null) {
            return "after/search";
        }
        return "default/search";
    }*/

    @GetMapping("/search") // 검색 기능
    public String search(@RequestParam("query") String query, @RequestParam("menu")Integer menu, Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (menu == 1) {
            List<Product> products = productService.productSearch(query);
            model.addAttribute("products", products);
        }



        if (authCookie != null) {
            return "after/search";
        }
        return "default/search";
    }
}
