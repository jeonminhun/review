package com.project.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class asdf {
    @Controller
    public class WebController {

        @RequestMapping("/")
        public String main(){

            return "index";
        }
    }
}
