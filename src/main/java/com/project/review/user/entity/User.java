package com.project.review.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.review.product.entity.Review;
import com.project.review.user.dto.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Table(name = "user")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false, name = "user_email")
    private String userEmail;

    @Column(nullable = false)
    private String user_password;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false, name = "user_role")
    @ColumnDefault("1")
    private int userRole;

    @Column
    private String user_info;

    @Column(nullable = false, name = "user_phone number")
    private String user_phoneNumber;

    @Column(nullable = false, name = "user_nick name")
    private String user_nickName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Review> review = new ArrayList<>();

    public String getRoleName() {
        log.info("엔티티 확인 : "+ this.userRole);
        return Role.getRoleByCode(this.userRole);
    }




}
