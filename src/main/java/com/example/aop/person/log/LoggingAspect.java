package com.example.aop.person.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Log)")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void logBeforeAllMethodsAdvice(JoinPoint joinPoint) {
//        logger.info("joinPoint -> " + joinPoint);
        logger.info(
                String.format("entering method %s, args: %s",
                        joinPoint.getSignature(),
                        Arrays.toString(joinPoint.getArgs()))
        );
    }

    @After("logPointcut()")
    public void logAfterAllMethodsAdvice(JoinPoint joinPoint) {
//        logger.info("joinPoint -> " + joinPoint);
        logger.info("exiting method " + joinPoint.getSignature());
    }

    @AfterReturning(value = "logPointcut()", returning = "result")
    public void logAfterReturningAllMethodsAdvice(Object result) {
        logger.info("returned value was {}", result);
    }

    @AfterThrowing(value = "logPointcut()", throwing = "ex")
    public void logAfterThrowingAllMethodsAdvice(Exception ex) {
        logger.warn("after throwing -> {}", ex.getLocalizedMessage());
    }

    @Around("within(com.example.aop.person.*)")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logger.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }
}
