package com.safeking.shop.global.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.exhandler.ErrorResult;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.jwt.refreshToken.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private ObjectMapper om;



    //로그인 시에 실행이 되는 필터
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication 실행");

        try {
            // 1. username,password를 받아서
            om = new ObjectMapper();
            Member member=om.readValue(request.getInputStream(), Member.class);

            // 2. 바탕으로 토큰을 만들어서
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            //3. loadByUsername을 실행
                Authentication authentication
                        = authenticationManager.authenticate(authenticationToken);
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

                log.info("principalDetails.name={}",principalDetails.getMember().getUsername());

                return authentication;

        } catch (RuntimeException e) {

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorResult errorResult = new ErrorResult("jwt_token_access", e.getMessage());

            om.writeValue(response.getWriter(),errorResult);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("successfulAuthentication 실행");

        log.info("일반로그인 user 에게 JWT 토큰 발행");
        Tokens tokens = tokenUtils.createTokens(authResult);

        response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
        response.addHeader(REFRESH_HEADER,tokens.getRefreshToken());
    }
}
