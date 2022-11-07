package com.safeking.shop.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class JwtPrincipal {

    private String accessToken;
    private Instant expiry;
    private Long userId;
}
