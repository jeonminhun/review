package com.project.review.user.controller;

import com.project.review.user.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Controller
public class user_controller {
    @GetMapping("/login")
    public String login_form(/*@RequestBody user_create_dto user_create_dto,*/ Model model) {
        return "login";

    }
    @GetMapping("/Register")
    public String create_form(/*@RequestBody user_create_dto user_create_dto,*/ Model model) {
        return "Register";

    }


}
