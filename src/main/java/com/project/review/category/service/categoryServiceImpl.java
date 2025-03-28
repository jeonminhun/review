package com.project.review.category.service;

import com.project.review.category.dto.categoryEnum;
import com.project.review.category.dto.categoryReviewDto;
import com.project.review.category.entity.Category;
import com.project.review.category.repository.categoryRepository;
import com.project.review.product.repository.productRepository;
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
    private final productRepository productRepository;

    @Override
    public List<categoryReviewDto> categoryReviewCount() {
        List<categoryReviewDto> categoryReviewDtos = new ArrayList<>();

        for (int i = 1; i < 12; i++) {
            String categoryByCode = categoryEnum.getCategoryByCode(i);
            log.info("테스트 : "+categoryByCode);
            Long count = categoryRepository.categoryReviewCount(i);
            log.info("테스트2 : "+count);
            categoryReviewDto reviewDto = new categoryReviewDto();
            reviewDto.setCategory(categoryByCode);
            reviewDto.setReviewCount(count);
            categoryReviewDtos.add(reviewDto);
        }

        return categoryReviewDtos;
    }
}
