package com.project.review.user.repository;

import com.project.review.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {

    Optional<User> findByuserEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userRole = :userRole WHERE u.user_id = :user_id")
    int updateUserRole(@Param("user_id") Long user_id, @Param("userRole") int userRole);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.user_name = :user_name WHERE u.user_id = :user_id")
    int UserUpdate(@Param("user_id") Long user_id,@Param("user_name")  String user_name);


}
