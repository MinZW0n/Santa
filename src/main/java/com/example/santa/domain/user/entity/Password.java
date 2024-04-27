package com.example.santa.domain.user.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Password {
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    private String encodePassword;

    public Password(final String rawPassword) {
        this.encodePassword = encodePassword(rawPassword);
    }

    private String encodePassword(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void changePassword(final String oldRawPassword, final String newRawPassword) {
        if (!isMatches(oldRawPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } else if (isMatches(newRawPassword)) {
            throw new IllegalArgumentException("기존 비밀번호로는 변경할 수 없습니다..");
        }
        this.encodePassword = encodePassword(newRawPassword);

    }

    public void findPassword(final String newRawPassword) {
        this.encodePassword = encodePassword(newRawPassword);
    }



    private boolean isMatches(String rawPassword) {
        return passwordEncoder.matches(rawPassword, encodePassword);
    }

}
