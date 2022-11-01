package com.safeking.shop.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class JwtToken {

    private String token;
    private Instant expiry;
}
