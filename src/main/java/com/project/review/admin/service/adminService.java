package com.project.review.admin.service;

import com.project.review.admin.dto.productCreateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface adminService {
     boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request);

}
