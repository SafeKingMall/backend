package com.safeking.shop.global.jwt.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.safeking.shop.global.exhandler.erroconst.ErrorConst.*;

@Slf4j
public class JwtAuthenticationExceptionFilter extends AbstractExceptionFilter {
    /**
     * 1. BasicAuthenticationFilter 의 exception 을 잡아줌
     * 2. custom error 형식으로 response
     **/
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
            generateErrorResponse(response, 401, new Error(JWT_DECODE_EX_CODE,"잘못된 jwt 양식입니다."));
        } catch (TokenExpiredException e){
            generateErrorResponse(response, 401, new Error(TOKEN_EXPIRED__EX_CODE,"토큰의 유효 시간이 만료 되었습니다."));
        } catch (SignatureVerificationException e){
            generateErrorResponse(response, 401, new Error(SIGNATURE_VERIFICATION_EX_CODE,"잘못된 서명입니다."));
        } catch (Exception e){
            generateErrorResponse(response, 401, new Error(JWT_EX_CODE,"jwt 인증에 문제가 있습니다."));
        }
    }
}
