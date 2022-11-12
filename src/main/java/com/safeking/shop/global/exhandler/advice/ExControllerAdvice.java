package com.safeking.shop.global.exhandler.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.exception.MemberNotFoundException;
import com.safeking.shop.global.exhandler.ErrorResult;
import com.safeking.shop.global.jwt.exception.RefreshTokenNotFoundException;
import com.safeking.shop.global.jwt.response.refresh.Data;
import com.safeking.shop.global.jwt.response.refresh.RefreshTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> illegalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex",e);

        ErrorResult errorResult = new ErrorResult("BAD", e.getMessage());
        return ResponseEntity
                .badRequest().body(errorResult);
    }
    @ExceptionHandler
    public ResponseEntity<RefreshTokenResponse> refreshTokenNotFoundExHandler(RefreshTokenNotFoundException e){
        log.error("[refreshTokenNotFoundExHandler] ex",e);

        Error error = new Error(e.getMessage(), 403);

        RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
                .code(0)
                .message("")
                .data(new Data(Data.DEFAULT))
                .error(error)
                .build();

        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);

        return new ErrorResult("EX","내부오류");
    }

}
