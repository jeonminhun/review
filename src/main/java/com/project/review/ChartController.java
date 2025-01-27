package com.project.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ChartController {

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
