package com.thy.todo.intercepter;

import com.thy.todo.common.TimeoutLimit;
import com.thy.todo.exception.CustomTimeoutException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Aspect
@Component
public class TimeoutInterceptor {

    @Around("@annotation(timeoutLimit)")
    public Object enforceTimeout(ProceedingJoinPoint joinPoint, TimeoutLimit timeoutLimit) throws Throwable {
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });

        try {
            return future.get(timeoutLimit.value(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new CustomTimeoutException(timeoutLimit.message());
        }
    }
}
