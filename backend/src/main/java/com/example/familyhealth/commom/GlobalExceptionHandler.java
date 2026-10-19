package com.example.familyhealth.commom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
    /**
     * 异常处理
     */
    @ExceptionHandler(Exception.class)
    public int globalException(Exception e) {
        e.printStackTrace();
        return 500;
    }
}
