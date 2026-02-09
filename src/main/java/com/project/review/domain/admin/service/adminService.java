package com.project.review.domain.admin.service;

import com.project.review.domain.admin.dto.UserGradeDto;
import com.project.review.domain.admin.dto.productCreateDto;
import com.project.review.domain.admin.entity.adminProductUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface adminService {
     boolean checkAdmin(HttpServletRequest request);

     boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request);

     boolean productDelete(Long product_id);

     boolean productUpdate(adminProductUpdateDto adminProductUpdateDto, MultipartFile files, HttpServletRequest request);

     boolean userGradeUpdate(UserGradeDto userGradeDto, HttpServletRequest request);

     boolean userDelete(Long user_id, HttpServletRequest request);

}
