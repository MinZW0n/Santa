package com.example.santa.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP 설정 클래스
@Component
public class LogAop {

    // 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
    @Pointcut("execution(* com.example.santa..*.controller.*.*(..))")
    private void cut(){}

    //cut() 메서드가 실행 되는 지점 이전에 before() 메서드 실행
//    @Before("cut()")
//    public void before(JoinPoint joinPoint){
//
//    }
    @Before("cut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[메서드 호출 전] 호출 메서드 {}", joinPoint.getSignature().getName());
    }

    @After("cut()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("[메서드 호출 후] 호출 메서드 {}", joinPoint.getSignature().getName());
    }

}
