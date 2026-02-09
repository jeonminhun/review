package com.project.review.domain.user.repository;

import com.project.review.domain.user.entity.UserImg;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface userImgRepository extends JpaRepository<UserImg,Long> {


    @Transactional
    @Query("select u from UserImg u where u.user.user_id = :user_id")
    UserImg findByUser_id(@Param("user_id") Long user_id);


}
