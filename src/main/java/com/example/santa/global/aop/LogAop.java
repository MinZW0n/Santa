package com.example.santa.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//@Slf4j
//@Aspect //AOP 설정 클래스
//@Component
//public class LogAop {
//    // 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
//    @Pointcut("execution(* com.example.santa..*.controller*.*(..))")
//    private void cut(){}
//
//    @Before()
//}
