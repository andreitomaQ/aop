package com.example.aop.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdderAspect {
    private final Logger logger = LoggerFactory.getLogger(AdderAspect.class);

    @Pointcut("execution(* com.example.aop.test.SampleAdder.*(..))")
    public void sampleAdderClass() {
    }

    @Before("sampleAdderClass()")
    public void before(JoinPoint joinPoint) {
        logger.info("joinPoint -> " + joinPoint);
        logger.info("entering method " + joinPoint.getSignature());
    }

    @After("sampleAdderClass()")
    public void after(JoinPoint joinPoint) {
        logger.info("joinPoint -> " + joinPoint);
        logger.info("exiting method " + joinPoint.getSignature());
    }

    @AfterReturning(value = "sampleAdderClass()",
            returning = "returnValue")
    public void afterReturn(Object returnValue) {
        logger.info("returned value was {}", returnValue);
    }
}
