package com.project.review.user.repository;


import com.project.review.user.entity.RefreshToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends KeyValueRepository<RefreshToken, String> {
    Optional<RefreshToken> findByKey(String key);
}