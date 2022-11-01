package com.safeking.shop.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.security.role.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

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

    public JwtToken createAccessToken(Long userId, String authorities) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenExpirySec);

        String accessTokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .claim("auth", authorities)
                .claim("uid", userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .compact();

        return new JwtToken(accessTokenValue, expiry);
    }

    public JwtToken createAccessToken(Long userId, Role role) {
        return createAccessToken(userId, role.getRoleValue());
    }

    public JwtToken createRefreshToken() {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenExpirySec);
        String refreshTokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .compact();

        return new JwtToken(refreshTokenValue, expiry);
    }
}
