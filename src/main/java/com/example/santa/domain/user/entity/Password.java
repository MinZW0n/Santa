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
        if (isMatches(oldRawPassword)) {
            this.encodePassword = encodePassword(newRawPassword);
        }
    }

    private boolean isMatches(String rawPassword) {
        return passwordEncoder.matches(rawPassword, encodePassword);
    }
}
