package com.project.review.admin.service;

import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.productAdminDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface adminService {
     boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request);

     boolean productDelete(productAdminDto productAdminDto);

     boolean productUpdate(productAdminDto productAdminDto, MultipartFile files, HttpServletRequest request);

}
