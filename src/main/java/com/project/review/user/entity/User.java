package com.project.review.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String user_email;

    @Column(nullable = false)
    private String user_password;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int user_role;
}
