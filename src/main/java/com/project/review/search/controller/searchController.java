package com.project.review.search.controller;

import com.project.review.category.dto.categoryReviewDto;
import com.project.review.category.service.categoryService;
import com.project.review.product.entity.Product;
import com.project.review.product.service.productService;
import com.project.review.user.entity.User;
import com.project.review.user.service.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class searchController {
    private final userService userService;
    private final productService productService;
    private final categoryService categoryService;

    @GetMapping("/search") // 검색 기능
    public String search(@RequestParam("query") String query,
                         @RequestParam("menu")Integer menu,
                         @RequestParam(value = "sort", required = false, defaultValue = "total") String sort,
                         @PageableDefault(size = 9) Pageable pageable,
                         Model model, HttpServletRequest request) {


        Sort sorting = switch (sort) {
            case "coef" -> Sort.by("product_coef_rating").descending();
            case "durability" -> Sort.by("product_durability_rating").descending();
            case "quality" -> Sort.by("product_quality_rating").descending();
            case "design" -> Sort.by("product_design_rating").descending();
            default -> Sort.by("product_total_rating").descending();
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);



        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        List<categoryReviewDto> categoryReviews = categoryService.categoryReviewCount();
        model.addAttribute("categoryReviews", categoryReviews);
        Page<Product> products;

        if (menu == 1) {
            products = productService.productSearch(query, sortedPageable);
        }else  {
            products = productService.productCategory(query, sortedPageable);
        }
        model.addAttribute("products", products);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("query", query);
        model.addAttribute("menu", menu);

        if (authCookie != null) {
            Long user_id = userService.getUserId(request);
            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);
            return "after/search";
        }
        return "default/search";
    }
}
