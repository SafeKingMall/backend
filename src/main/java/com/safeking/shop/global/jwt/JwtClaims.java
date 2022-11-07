package com.safeking.shop.global.jwt;

import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class JwtClaims {

    private static final String CLAIM_NAME_USER_ID = "uid";
    private static final String CLAIM_NAME_AUTHORIZATION = "auth";
    private static final String CLAIM_NAME_ISSUED_AT = Claims.ISSUED_AT;
    private static final String CLAIM_NAME_EXPIRATION = Claims.EXPIRATION;

    private Long userId;
    private String auth;
    private Instant issuedAt;
    private Instant expiry;

    public JwtClaims(Map<String, Object> payloadMap) {
        Object uidObj = payloadMap.get(CLAIM_NAME_USER_ID);
        if (uidObj != null) {
            this.userId = Long.parseLong(uidObj.toString());
        }

        Object authObj = payloadMap.get(CLAIM_NAME_AUTHORIZATION);
        if (authObj != null) {
            this.auth = authObj.toString();
        }

        Object iatObj = payloadMap.get(CLAIM_NAME_ISSUED_AT);
        if (iatObj != null) {
            this.issuedAt = toInstant(iatObj);
        }

        Object expObj = payloadMap.get(CLAIM_NAME_EXPIRATION);
        if (expObj != null) {
            this.expiry = toInstant(expObj);
        }
    }

    @Builder
    public JwtClaims(Long userId, String auth, Instant issuedAt, Instant expiry) {
        this.userId = userId;
        this.auth = auth;
        this.issuedAt = issuedAt;
        this.expiry = expiry;
    }

    private Instant toInstant(Object obj) {
        return Instant.ofEpochMilli(Long.parseLong(obj.toString()) * 1000);
    }

    public Map<String, Object> getClaimsMap() {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put(CLAIM_NAME_USER_ID, userId);
        claims.put(CLAIM_NAME_AUTHORIZATION, auth);
        claims.put(CLAIM_NAME_ISSUED_AT, Date.from(issuedAt));
        claims.put(CLAIM_NAME_EXPIRATION, Date.from(expiry));

        return claims;
    }
}
