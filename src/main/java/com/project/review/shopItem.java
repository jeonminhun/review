package com.project.review;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.WebUtils;

@Controller
public class shopItem {
        @RequestMapping("/shop")
        public String main(Model model, HttpServletRequest request){
            Cookie authCookie = WebUtils.getCookie(request, "Authorization");
            if (authCookie != null) {
                return "after/product-details";
            }
            model.addAttribute("data","hi my name is review!");
            return "default/product-details";
        }

}
