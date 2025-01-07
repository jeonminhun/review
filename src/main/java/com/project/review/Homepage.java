package com.project.review;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Homepage {
    @RequestMapping("/")
    public String main(Model model, HttpServletRequest request){
        /*if (request.getCookies().length == 3) {
            return "바뀐거";
        }*/
        model.addAttribute("data","hi my name is review!");
        return "default/index";
    }

}
