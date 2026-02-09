package com.project.review.global.util;

import com.project.review.domain.user.entity.User;
import com.project.review.domain.user.entity.UserImg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class FileService {
    // 이미지 파일 저장, 삭제 메소드 구조 개선 및 통합 및 별도 디렉토리 및 클래스 분리

    private final String rootPath = "src/main/resources/static/assets/images/";

    public void imgDelete(String domain, String fileName) {
        try {
            Path uploadPath = Path.of(rootPath, domain);
            Path filepath = uploadPath.resolve(fileName);
            Files.delete(filepath);
        } catch (Exception e) {
            log.info("이미지 삭제 오류");
            e.printStackTrace();
        }
    }

    public String imgSave(MultipartFile files, String domain, String fileName) {
        log.info("로그확인");
        if (files == null || files.isEmpty()) return null;
        log.info("로그확인2");

        try {
            Path uploadPath = Path.of(rootPath, domain);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
//                String fileExtension = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));

            log.info("유저 이미지 파일 네임 메소드 : " + fileName);

            Path filepath = uploadPath.resolve(fileName);
            Files.copy(files.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING /*같은 파일이름 있으면 덮어쓰기*/);

            return fileName;

        } catch (Exception e) {
            log.info("이미지 저장 오류");
            e.printStackTrace();
            return null;
        }
    }
}
