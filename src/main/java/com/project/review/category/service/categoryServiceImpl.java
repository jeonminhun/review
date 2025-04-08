package com.project.review.category.service;

import com.project.review.category.dto.categoryEnum;
import com.project.review.category.dto.categoryReviewDto;
import com.project.review.category.repository.categoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class categoryServiceImpl implements categoryService {
    private final categoryRepository categoryRepository;

    @Override
    public List<categoryReviewDto> categoryReviewCount() {
        List<categoryReviewDto> categoryReviewDtos = new ArrayList<>();

        for (int i = 1; i < 12; i++) {
            String categoryByCode = categoryEnum.getCategoryByCode(i);
            Long count = categoryRepository.categoryReviewCount(i);
            categoryReviewDto reviewDto = new categoryReviewDto();
            reviewDto.setCategory(categoryByCode);
            reviewDto.setReviewCount(count);
            categoryReviewDtos.add(reviewDto);
        }

        return categoryReviewDtos;
    }
}
