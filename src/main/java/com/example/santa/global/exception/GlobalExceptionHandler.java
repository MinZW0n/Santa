package com.example.santa.global.exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceLogicException.class)
    public ResponseEntity<Object> handleServiceLogicException(ServiceLogicException ex) {
        ExceptionCode code = ex.getExceptionCode();
        ErrorResponse errorResponse = new ErrorResponse(code.getStatus(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, code.getStatus());
    }

    // ErrorResponse 클래스 (내부 클래스로 정의)
    @Getter
    private static class ErrorResponse {
        // Getters
        private final HttpStatus status;
        private final String message;

        public ErrorResponse(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

    }

    // MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // 오류 메시지를 담을 Map 생성
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 상태 코드 400(Bad Request)와 함께 오류 메시지를 반환
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}