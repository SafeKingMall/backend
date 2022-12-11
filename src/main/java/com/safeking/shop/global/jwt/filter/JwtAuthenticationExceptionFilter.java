package com.safeking.shop.global.jwt.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationExceptionFilter extends AbstractExceptionFilter {

    public JwtAuthenticationExceptionFilter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JWTDecodeException e){
            generateErrorResponse(response, 401, new Error(811,"잘못된 jwt 양식입니다."));
        } catch (TokenExpiredException e){
            generateErrorResponse(response, 401, new Error(812,"토큰의 유효 시간이 만료 되었습니다."));
        } catch (SignatureVerificationException e){
            generateErrorResponse(response, 401, new Error(813,"잘못된 서명입니다."));
        } catch (Exception e){
            generateErrorResponse(response, 401, new Error(815,"jwt 인증에 문제가 있습니다."));
        }
    }
}
