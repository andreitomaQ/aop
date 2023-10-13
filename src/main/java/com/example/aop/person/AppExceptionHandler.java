package com.example.aop.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public void runtimeExceptionHandler(RuntimeException exception) {
        log.warn(exception.getLocalizedMessage());
    }
}
