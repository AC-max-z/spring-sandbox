package com.maxzamota.spring_sandbox.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    @Around("execution(* com.maxzamota.spring_sandbox.controllers.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long end = System.currentTimeMillis();
            log.info("User {} executed controller method {} in {} ms",
                    keyValue("username", username),
                    joinPoint.getSignature().toShortString(),
                    end - start
            );
        }
    }
}
