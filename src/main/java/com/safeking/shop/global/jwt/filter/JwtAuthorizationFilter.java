package com.safeking.shop.global.jwt.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.auth.PrincipalDetailsRedis;
import com.safeking.shop.global.exception.CacheException;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    /**
     * 1. 권한처리시 사용되는 필터
     * 2. jwt 바탕으로 Authentication 영역의 redisMember 객체의 username 을 조회
     * 3. username 바탕으로 권한처리
     **/

    private MemberRedisRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRedisRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
    ) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        log.info("JwtAuthorizationFilter 실행, 인증이 필요");
        
        String jwtHeader=request.getHeader(AUTH_HEADER);

        if(jwtHeader==null||!jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);//다른 필터는 타되 다 타면 종료
            return;
        }

        String jwToken=request.getHeader(AUTH_HEADER).replace(BEARER,"");

        String username= JWT.require(Algorithm.HMAC512("safeKing")).build()
                    .verify(jwToken).getClaim("username").asString();

        if(username!=null){

            RedisMember redisMember = memberRepository.findByUsername(username).orElseThrow(() -> new CacheException("캐시에 문제가 있습니다."));
            PrincipalDetailsRedis principalDetails = new PrincipalDetailsRedis(redisMember);

            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());
            //시큐리티 세션=> 권한처리
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);
    }

}
