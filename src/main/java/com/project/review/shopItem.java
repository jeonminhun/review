package com.project.review;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @ResponseBody
    @GetMapping("/chart-data")
    public Map<String, Object> getChartData() {
        log.info("차트 데이터 호출");

        Map<String, Object> chartData = new HashMap<>();

        // 데이터와 레이블을 준비
        chartData.put("data", Arrays.asList(12, 19, 30, 40, 60)); // 차트에 들어갈 데이터
        chartData.put("labels", Arrays.asList("총 별점", "가성비 별점", "품질 별점", "내구성 별점", "디자인 별점")); // X축 레이블
        chartData.put("total", Arrays.asList(5, 10, 10, 20, 25, 30)); // 차트에 들어갈 데이터

        return chartData; // JSON 형식으로 반환
    }

}
