package com.project.review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Homepage {
        @RequestMapping("/")
        public String main(Model model){
            model.addAttribute("data","hi my name is review!");
            return "index";
        }
    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("data","hi my name is review!");
        return "index";
    }

}
