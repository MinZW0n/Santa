package com.example.santa.global.exception;

import lombok.Getter;

@Getter
public class ServiceLogicException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public ServiceLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
