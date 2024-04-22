package com.example.santa.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "모임을 찾을 수 없습니다."),
    ALREADY_PARTICIPATING(HttpStatus.CONFLICT, "이미 참여중인 모임입니다" );


    private final HttpStatus status;
    private final String message;

    ExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
