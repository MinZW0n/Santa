package com.example.santa.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Aspect //AOP 설정 클래스
@Component
public class LogAop {

    // 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
    @Pointcut("execution(* com.example.santa..*.controller.*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void logBefore(JoinPoint joinPoint) {
        //메서드 이름 호출
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("[ 호출 전] 호출 메서드 {}", method.getName());

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.info("매개변수가 없습니다.");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    log.info("매개변수 타입 = {}, 매개변수 값 = {}", arg.getClass().getSimpleName(), arg);
                } else {
                    log.info("매개변수 값 = null");
                }
            }
        }

        // 현재 HTTP 요청에 접근
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // HTTP 메서드와 URL 로깅
            log.info("HTTP 메서드: {}", request.getMethod());
            log.info("URL: {}", request.getRequestURL().toString());

            // 모든 쿼리 스트링 파라미터 로깅
            if (request.getQueryString() != null) {
                log.info("쿼리 스트링: {}", request.getQueryString());
            }

            // 쿼리 스트링 및 폼 데이터에서 모든 파라미터 로깅
            Map<String, String[]> parameters = request.getParameterMap();
            parameters.forEach((key, value) -> log.info("파라미터: {}, 값: {}", key, Arrays.toString(value)));


            // 특정 파라미터 추출 (예: "address")
            String[] addressValues = parameters.get("address");
            if (addressValues != null) {
                log.info("주소: {}", Arrays.toString(addressValues));
            }

            // SecurityContextHolder에서 유저 정보 탐색
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                log.info("유저 : {}", username);
            } else {
                log.info("인증된 사용자가 없습니다.");
            }
        } else {
            log.info("ServletRequestAttributes 가 null입니다.");
        }
    }


    // Poincut에 의해 필터링된 경로로 들어오는 경우 메서드 리턴 후에 적용
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void logAfter(JoinPoint joinPoint, Object returnObj) {
        //메서드 정보 받아오기, 메서드 이름 호출
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        log.info("[메서드 호출 후] 호출 메서드 {}", method.getName());

        log.info("return type = {}", returnObj.getClass().getSimpleName());
        log.info("return value = {}", returnObj);
    }


}
