package com.safeking.shop.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.refreshToken.RefreshToken;
import com.safeking.shop.global.jwt.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        String refreshToken=request.getHeader(REFRESH_HEADER);

        if(refreshToken==null|refreshTokenRepository.findByRefreshToken(refreshToken)==null){
            log.info("refresh토큰이 없습니다.");
        }

        String username = verify(refreshToken);

        if(username!=null){

            Member member = memberRepository.findByUsername(username).orElseThrow();

            PrincipalDetails principalDetails = new PrincipalDetails(member);

            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());
            //시큐리티 세션=> 권한처리
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Tokens tokens = tokenUtils.createTokens(authentication);
            log.info("refreshController Rtoken={}",tokens.getRefreshToken());

            response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
            response.addHeader(REFRESH_HEADER,tokens.getRefreshToken());

        }

    }

}
