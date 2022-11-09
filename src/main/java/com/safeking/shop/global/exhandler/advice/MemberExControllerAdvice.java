package com.safeking.shop.global.exhandler.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.safeking.shop.global.exception.MemberNotFoundException;
import com.safeking.shop.global.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MemberExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> illegalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex",e);

        ErrorResult errorResult = new ErrorResult("BAD", e.getMessage());
        return ResponseEntity
                .badRequest().body(errorResult);
    }



    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);

        return new ErrorResult("EX","내부오류");
    }

}
