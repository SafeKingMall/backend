package com.safeking.shop.domain.item.web.controller.exception;

import com.safeking.shop.domain.exception.ItemException;
import com.safeking.shop.global.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ItemExceptionController {
    @ExceptionHandler(ItemException.class)
    public ResponseEntity<Error> itemExHandler(ItemException e) {
        log.error("[ItemException] ", e);
        Error error = new Error(3000, e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
