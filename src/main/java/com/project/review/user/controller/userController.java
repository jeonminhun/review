package com.project.review.user.controller;

import com.project.review.user.dto.userCreateDto;
import com.project.review.user.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class userController {

    private final userService userService;

    @GetMapping("/Register")
    public String create_form(Model model) {
        userCreateDto userCreateDto = new userCreateDto();
        model.addAttribute("userCreateDto",userCreateDto);
        return "Register";

    }

    @PostMapping("/Register")
    public String create_form(@ModelAttribute userCreateDto userCreateDto, BindingResult bindingResult, Model model) {
        if (!userService.checkPassWord(userCreateDto)) {
            bindingResult.addError(new FieldError("userCreateDto","user_password","비밀번호가 다릅니다."));
            return "Register";
        }
        if (bindingResult.hasErrors()) { return "Register"; }
        return "redirect:/login";

    }

    @GetMapping("/login")
    public String login_form(Model model) {
        return "login";

    }
    @PostMapping("/login")
    public String login_form(userCreateDto userCreateDto, Model model) {
        return "login";

    }



}
