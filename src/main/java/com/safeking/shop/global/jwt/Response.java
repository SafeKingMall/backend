package com.safeking.shop.global.jwt;

import lombok.Data;

@Data
class Response {
    String jwtToken;
    String refreshToken;

    public Response(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }
}
