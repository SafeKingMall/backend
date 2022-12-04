package com.safeking.shop.global.jwt.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.domain.user.domain.repository.CacheMemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.exception.CacheException;
import lombok.extern.slf4j.Slf4j;
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
//권한처리시 사용되는 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
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

            Member member = memberRepository.findByUsername(username).orElseThrow(()->new CacheException("캐시에 문제가 있습니다."));

            PrincipalDetails principalDetails = new PrincipalDetails(member);

            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());
            //시큐리티 세션=> 권한처리
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        chain.doFilter(request,response);
    }
}
