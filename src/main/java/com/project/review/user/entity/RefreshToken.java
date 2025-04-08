package com.project.review.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash (value = "refresh", timeToLive = 604800)
public class RefreshToken {

    @Id
    private String key;

    private String value;

    private String ip;

    @Builder
    public RefreshToken(String key, String value, String ip) {
        this.key = key;
        this.value = value;
        this.ip = ip;
    }

    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}