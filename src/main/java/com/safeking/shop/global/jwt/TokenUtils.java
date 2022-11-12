package com.safeking.shop.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.refreshToken.RefreshToken;
import com.safeking.shop.global.jwt.refreshToken.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Component
public class TokenUtils {
    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "refresh-token";
    public static final String BEARER = "Bearer ";
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenUtils(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
    public static enum TokenType {
        access,
        refresh
    }
    public static String verify(String token){

        return JWT.require(Algorithm.HMAC512("safeKing")).build()
                .verify(token).getClaim("username").asString();

    }

    public String generate(Authentication authentication,TokenType tokenType){
        log.info("generate!!");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(getLifeTime(tokenType))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getUsername())
                .sign(Algorithm.HMAC512("safeKing"));
        log.info("token= {} ",token);
        return token;
    }

    public Tokens createTokens(Authentication authentication){
        String jwtToken = generate(authentication, TokenType.access);
        String refreshToken = generate(authentication, TokenType.refresh);

        refreshTokenRepository.save(new RefreshToken(refreshToken,jwtToken));

        return new Tokens(jwtToken, refreshToken);
    }

    private Date getLifeTime(TokenType type) {
        switch(type){
            case refresh:
                return new Date(System.currentTimeMillis() + (60000 * 10));
            case access:
                return new Date(System.currentTimeMillis() + (60000 * 100));
        }
        return null;
    }






}
