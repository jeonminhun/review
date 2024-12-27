package com.project.review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class shop_item {
        @RequestMapping("/shop")
        public String main(Model model){
            model.addAttribute("data","hi my name is review!");
            return "product-details";
        }

}
