package com.project.review.mypage.controller;

import com.project.review.product.entity.Review;
import com.project.review.product.entity.Save;
import com.project.review.product.service.productService;
import com.project.review.user.entity.User;
import com.project.review.user.service.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class myPageController {
    private final userService userService;
    private final productService productService;

    @GetMapping("/myPage/{user_id}")
    public String myReview(@PathVariable("user_id")Long user_id, Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            List<Review> reviews = productService.myReview(user_id, request);
            model.addAttribute("user_id",user_id);
            model.addAttribute("reviews",reviews);
            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);
            return "after/myReview";
        }
        return "default/index";


    }
    @GetMapping("/myData/{user_id}")
    public String myPage(@PathVariable("user_id")Long user_id, Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);
            return "after/mypage";
        }
        return "default/index";


    }
    @GetMapping("/myWish/{user_id}")
    public String myWish(@PathVariable("user_id")Long user_id, Model model, HttpServletRequest request) {
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        if (authCookie != null) {
            List<Save> saves = productService.saveInfo(user_id, request);
            model.addAttribute("saves",saves);
            model.addAttribute("user_id",user_id);
            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);
            return "after/myWishProduct";
        }
        return "default/index";


    }
}
