package com.safeking.shop.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class TokenUtils {
    /**
     * 토큰과 관련된 로직을 모아 Utils 생성
     **/
    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "refresh-token";
    public static final String BEARER = "Bearer ";
    private static final String PRIVATE_KEY="safeKing";

    public static enum TokenType {
        access,
        refresh
    }
    public static String verify(String token){

        return JWT.require(Algorithm.HMAC512(PRIVATE_KEY)).build()
                .verify(token).getClaim("username").asString();

    }

    public static String getUsername(HttpServletRequest request){
        String jwtHeader=request.getHeader(AUTH_HEADER);

        if(jwtHeader==null||!jwtHeader.startsWith("Bearer")){
            throw new TokenNotFoundException("jwt 토큰이 없습니다.");
        }

        String jwToken=request.getHeader(AUTH_HEADER).replace(BEARER,"");

        return JWT.require(Algorithm.HMAC512("safeKing")).build()
                .verify(jwToken).getClaim("username").asString();
    }

    public String generate(Authentication authentication,TokenType tokenType){
        log.info("generate!!");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(getLifeTime(tokenType))
                .withClaim("id", principalDetails.getMember().getId())
                .withClaim("username", principalDetails.getMember().getUsername())
                .sign(Algorithm.HMAC512(PRIVATE_KEY));
        log.info("token= {} ",token);
        return token;
    }

    public Tokens createTokens(Authentication authentication){

        String jwtToken = generate(authentication, TokenType.access);
        String refreshToken = generate(authentication, TokenType.refresh);

        return new Tokens(jwtToken, refreshToken);
    }

    private Date getLifeTime(TokenType type) {
        switch(type){
            case refresh:
                return new Date(System.currentTimeMillis() + (60000 * 60));
            case access:
                return new Date(System.currentTimeMillis() + (60000 * 1)); // 60000 * 10 : 10분
        }
        return null;
    }






}
