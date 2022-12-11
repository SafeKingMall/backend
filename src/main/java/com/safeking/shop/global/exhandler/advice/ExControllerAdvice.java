package com.safeking.shop.global.exhandler.advice;

import com.safeking.shop.global.Error;
import com.safeking.shop.global.exception.AgreementException;
import com.safeking.shop.global.exception.MemberNotFoundException;
import com.safeking.shop.global.exception.CacheException;
import com.safeking.shop.global.jwt.exception.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Error> illegalExHandler(IllegalArgumentException e){
        log.error("[illegalExHandler] ex",e);

        return ResponseEntity
                .badRequest().body(new Error(100,e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Error> refreshTokenNotFoundExHandler(TokenNotFoundException e){
        log.error("[refreshTokenNotFoundExHandler] ex",e);

        return new ResponseEntity<>(new Error(200,e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<Error> CacheExceptionExHandler(CacheException e){
        log.error("[EntityNotFoundException] ex",e);

        return new ResponseEntity<>(new Error(201,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Error> EntityNotFoundExceptionExHandler(EntityNotFoundException e){
        log.error("[EntityNotFoundException] ex",e);

        return new ResponseEntity<>(new Error(300,e.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<Error> EntityExistsExceptionExHandler(EntityExistsException e){
        log.error("[EntityExistsException] ex",e);

        return new ResponseEntity<>(new Error(301,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> processValidationError(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]"+System.lineSeparator());
        }

        return new ResponseEntity<>(
                new Error(400,builder.toString()),HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Error exHandler(Exception e){
        log.error("[exceptionHandler] ex",e);

        return new Error(999,e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Error> usernameNotFoundExHandler(UsernameNotFoundException e){
        log.error("[usernameNotFoundExHandler] ex",e);

        return new ResponseEntity<>(
                new Error(1100,e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> MemberNotFoundExceptionExHandler(MemberNotFoundException e){
        log.error("[MemberNotFoundExceptionExHandler] ex",e);

        return new ResponseEntity<>(new Error(1200,e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<Error> AgreementExceptionExHandler(AgreementException e){
        log.error("[AgreementExceptionExHandler] ex",e);

        return new ResponseEntity<>(new Error(1111,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
