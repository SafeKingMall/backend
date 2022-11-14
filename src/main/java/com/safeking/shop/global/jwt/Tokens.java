package com.safeking.shop.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tokens {

    private String jwtToken;
    private String refreshToken;

}
