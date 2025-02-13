package com.project.review.user.repository;

import com.project.review.user.entity.UserImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userImgRepository extends JpaRepository<UserImg,Long> {
}
