package com.safeking.shop.global.jwt.controller;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.jwt.exception.RefreshTokenNotFoundException;
import com.safeking.shop.global.jwt.refreshToken.RefreshToken;
import com.safeking.shop.global.jwt.refreshToken.RefreshTokenRepository;
import com.safeking.shop.global.jwt.response.refresh.Data;
import com.safeking.shop.global.jwt.response.refresh.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final TokenUtils tokenUtils;

    @GetMapping("/api/v1/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){

        String refreshToken=request.getHeader(REFRESH_HEADER);

        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElse(null);

        if(refreshToken==null|findRefreshToken==null){
            throw new RefreshTokenNotFoundException("Refresh Token 이 유효하지 않습니다.");
        }

        String username = verify(refreshToken);

        if(username!=null){
            //authentication 을 생성
            Authentication authentication = createAuthentication(username);

            //기존 refreshToken 을 제거
            refreshTokenRepository.delete(findRefreshToken);

            Tokens tokens = tokenUtils.createTokens(authentication);

            response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
            response.addHeader(REFRESH_HEADER,tokens.getRefreshToken());

        }
        RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
                .code(200)
                .message(RefreshTokenResponse.SUCCESS_MESSAGE)
                .data(new Data(Data.DEFAULT))
                .error(new Error())
                .build();

        return ResponseEntity.ok().body(refreshTokenResponse);

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
