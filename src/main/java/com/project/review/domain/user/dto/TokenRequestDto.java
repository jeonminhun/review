package com.project.review.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;


}
