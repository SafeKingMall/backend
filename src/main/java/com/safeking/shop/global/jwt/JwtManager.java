package com.safeking.shop.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.global.security.role.Role;
import com.safeking.shop.global.security.role.RoleFactory;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Component
public class JwtManager {

    private final ObjectMapper objectMapper;

    private final String jwtSecretKey;
    private final long accessTokenExpirySec;
    private final long refreshTokenExpirySec;
    private final long reissueRefreshTokenCriteriaSec;

    public JwtManager(ObjectMapper objectMapper,
                      @Value("${jwt.secret}") String jwtSecretKey,
                      @Value("${jwt.access-token.expire-time}") long accessTokenExpirySec,
                      @Value("${jwt.refresh-token.expire-time}") long refreshTokenExpirySec,
                      @Value("${jwt.refresh-token.reissue-criteria}") long reissueRefreshTokenCriteriaSec) {
        this.objectMapper = objectMapper;

        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenExpirySec = accessTokenExpirySec;
        this.refreshTokenExpirySec = refreshTokenExpirySec;
        this.reissueRefreshTokenCriteriaSec = reissueRefreshTokenCriteriaSec;
    }

    public boolean validationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims != null;
        } catch (JwtException e) {
            return false;
        }
    }

    public JwtClaims getJwtClaims(String token) {
        String[] split = token.split("\\.");
        String jwtPayload = split[1];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(jwtPayload));
        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.readValue(payload, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            // TODO 예외 처리
            throw new RuntimeException(e);
        }
        return new JwtClaims(payloadMap);
    }

    public JwtToken createAccessToken(Long userId, String authorities) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenExpirySec);
        JwtClaims jwtClaims = JwtClaims.builder()
                .userId(userId)
                .auth(authorities)
                .issuedAt(now)
                .expiry(expiry)
                .build();

        String accessTokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .setClaims(jwtClaims.getClaimsMap())
                .compact();

        return new JwtToken(accessTokenValue, expiry);
    }

    public JwtToken createAccessToken(Long userId, MemberStatus status) {
        Role role = RoleFactory.getRoleByMemberStatus(status);
        return createAccessToken(userId, role.getRoleValue());
    }

    public JwtToken createRefreshToken() {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenExpirySec);
        JwtClaims jwtClaims = JwtClaims.builder()
                .issuedAt(now)
                .expiry(expiry)
                .build();
        String refreshTokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .setClaims(jwtClaims.getClaimsMap())
                .compact();

        return new JwtToken(refreshTokenValue, expiry);
    }
}
