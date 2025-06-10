package com.piseth.java.school.roomservice.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

	@Around("execution(* com.piseth.java.school.roomservice.service..*.*(..))")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info(joinPoint.getSignature().toString() + " method execution start");
		Instant start = Instant.now();
		Object returnObj = joinPoint.proceed();
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		log.info("Time took to execute " + joinPoint.getSignature().toString() + " method is : " + timeElapsed);
		log.info(joinPoint.getSignature().toString() + " method execution end");
		return returnObj;
	}
	
    @Pointcut("execution(* com.piseth.java.school.roomservice.service..*.*(..))")
    public void controllerMethods() {}
    
	@Before("controllerMethods()")
    public void logRequestBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("Request Body: {}", args[0]);
        }
    }


	@Around("execution(* com.piseth.java.school.roomservice.service..*.*(..))")
	public void logException(JoinPoint joinPoint, Exception ex) {
		log.error(joinPoint.getSignature() + " An exception happened due to : " + ex.getMessage());
	}

}
