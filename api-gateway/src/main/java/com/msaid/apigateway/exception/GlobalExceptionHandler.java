package com.msaid.apigateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleGeneralException(Throwable throwable){
        return ResponseEntity.badRequest()
                .body(throwable.getLocalizedMessage());
    }
}
