package com.maxzamota.spring_sandbox.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.maxzamota.spring_sandbox.service.*.*(..))")
    public void logBeforeExecution(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        log.info("User {} entering method {}",
                keyValue("username", username),
                joinPoint.getSignature().toShortString()
        );
    }

    @After("execution(* com.maxzamota.spring_sandbox.service.*.*(..))")
    public void logAfterExecution(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        log.info("User {} leaving method {}",
                keyValue("username", username),
                joinPoint.getSignature().toShortString()
        );
    }
}
