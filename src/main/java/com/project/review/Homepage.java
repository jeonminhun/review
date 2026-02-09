package com.project.review;

import com.project.review.domain.product.entity.Product;
import com.project.review.domain.product.service.productService;
import com.project.review.domain.user.entity.User;
import com.project.review.domain.user.service.userService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Homepage {
    private final userService userService;
    private final productService productService;
    @RequestMapping("/")
    public String main(Model model, HttpServletRequest request){
        log.info("홉 화면 접근");
        Cookie authCookie = WebUtils.getCookie(request, "Authorization");
        List<Product> productRanks = productService.productRank();
        model.addAttribute("productRanks", productRanks);
        List<Product> productSlides = productService.productSlide();
        model.addAttribute("productSlides", productSlides);

        if (authCookie != null) {
            Long user_id = userService.getUserId(request);
            User user = userService.userInfo(user_id, request);
            model.addAttribute("user", user);
            return "after/index";
        }
        model.addAttribute("data","hi my name is review!");
        return "default/index";
    }

}
