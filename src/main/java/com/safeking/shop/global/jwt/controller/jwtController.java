package com.safeking.shop.global.jwt.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class jwtController {
    private final MemberRepository memberRepository;
    private final TokenUtils tokenUtils;

    /**
     * 토큰이 만료시 client 쪽에 refresh API 로 jwt 와 refreshToken 을 요청
     **/
    @GetMapping("/api/v1/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        String username = getUsername(request);

        if(username!=null){
            //1. authentication 을 생성
            Authentication authentication = createAuthentication(username);
            //2. 토큰을 만들어서
            Tokens tokens = tokenUtils.createTokens(authentication);
            //3.header 에 담음
            response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
            response.addHeader(REFRESH_HEADER,BEARER+tokens.getRefreshToken());
        }
    }

    private static String getUsername(HttpServletRequest request) {

        if(request.getHeader(REFRESH_HEADER) ==null||!request.getHeader(REFRESH_HEADER).startsWith("Bearer"))
            throw new TokenNotFoundException("토큰이 없습니다.");

        String jwToken= request.getHeader(REFRESH_HEADER).replace(BEARER,"");

        String username = JWT.require(Algorithm.HMAC512("safeKing")).build()
                .verify(jwToken).getClaim("username").asString();
        return username;
    }

    private Authentication createAuthentication(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

}
