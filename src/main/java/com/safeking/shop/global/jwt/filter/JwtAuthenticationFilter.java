package com.safeking.shop.global.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
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
import java.util.List;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private ObjectMapper om;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;

        setFilterProcessesUrl("/api/v1/login");
    }

    //로그인 시에 실행이 되는 필터
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication함수 작동");


        try {
//            BufferedReader br=request.getReader();
//            String input=null;
//            while ((input=br.readLine())!=null){
//                System.out.println(input);
//            }

            // 1. username,password를 받아서
            ObjectMapper om = new ObjectMapper();
            LoginRequestDto user=om.readValue(request.getInputStream(),LoginRequestDto.class);
            System.out.println("user = " + user);
            // 2. 바탕으로 토큰을 만들어서
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            //3. loadByUsername을 실행
            Authentication authentication
                    = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("일반로그인 user 에게 JWT 토큰 발행");
        Tokens tokens = tokenUtils.createTokens(authResult);

        response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
        response.addHeader(REFRESH_HEADER,tokens.getRefreshToken());


    }

    private void generateResponseData(HttpServletResponse response) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
    }
}