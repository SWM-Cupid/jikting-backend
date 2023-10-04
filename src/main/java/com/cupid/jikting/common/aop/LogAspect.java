package com.cupid.jikting.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Before(value = "within(com.cupid.jikting..controller.*)")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("메소드 선언부 : {}, 전달 파라미터 : {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }
}
