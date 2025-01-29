package com.project.review.admin.controller;

import com.project.review.admin.dto.productCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

@Slf4j
@Controller
public class adminController { // 사진 받는것도 추가 해야함 + userId 어떻게 할지(받을지) 생각하기
    public String productInsert(@RequestBody productCreateDto productCreateDto) {

        return "redirect:/";
    }
}
