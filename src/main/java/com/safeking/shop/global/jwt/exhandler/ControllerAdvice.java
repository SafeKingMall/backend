package com.safeking.shop.global.jwt.exhandler;

import com.safeking.shop.global.Error;
import com.safeking.shop.global.exhandler.ErrorResult;
import com.safeking.shop.global.jwt.exception.RefreshTokenNotFoundException;
import com.safeking.shop.global.jwt.response.refresh.Data;
import com.safeking.shop.global.jwt.response.refresh.RefreshTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<RefreshTokenResponse> refreshTokenNotFoundExHandler(RefreshTokenNotFoundException e){
        log.error("[refreshTokenNotFoundExHandler] ex",e);

        Error error = new Error(e.getMessage(), 403);

        RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
                .code(0)
                .message(RefreshTokenResponse.SUCCESS_MESSAGE)
                .data(new Data(Data.DEFAULT))
                .error(error)
                .build();

        return new ResponseEntity<>(refreshTokenResponse, HttpStatus.FORBIDDEN);
    }

}
