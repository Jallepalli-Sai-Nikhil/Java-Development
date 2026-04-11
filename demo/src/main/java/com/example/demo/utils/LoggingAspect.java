package com.example.demo.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all controller methods
    @Pointcut("execution(* com.example.controller..*(..))")
    public void controllerLayer() {
    }

    // Pointcut for all service methods
    @Pointcut("execution(* com.example.service..*(..))")
    public void serviceLayer() {
    }

    // 🔹 Before method execution
    @Before("controllerLayer() || serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("➡️ Entering: {}.{}() with arguments = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // 🔹 After returning
    @AfterReturning(pointcut = "controllerLayer() || serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("✅ Exiting: {}.{}() with result = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }

    // 🔹 After throwing exception
    @AfterThrowing(pointcut = "controllerLayer() || serviceLayer()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        logger.error("❌ Exception in: {}.{}() with message = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }

    // 🔹 Around (measure execution time)
    @Around("controllerLayer() || serviceLayer()")
    public Object logExecutionTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // execute method

        long timeTaken = System.currentTimeMillis() - start;

        logger.info("⏱️ {}.{}() executed in {} ms",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                timeTaken);

        return result;
    }
}