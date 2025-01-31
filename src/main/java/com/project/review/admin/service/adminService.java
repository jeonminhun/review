package com.project.review.admin.service;

import com.project.review.admin.dto.productCreateDto;
import com.project.review.admin.entity.productDeleteDto;
import com.project.review.product.dto.productDto;
import com.project.review.product.dto.productImgDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

public interface adminService {
     boolean productCreate(productCreateDto productCreateDto, MultipartFile files, HttpServletRequest request);

     boolean productDelete(productDeleteDto productDeleteDto);

}
