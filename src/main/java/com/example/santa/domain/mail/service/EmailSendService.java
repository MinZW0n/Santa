package com.example.santa.domain.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.io.UnsupportedEncodingException;

public interface EmailSendService {
    String sendSimpleMessage(String to)throws MessagingException, UnsupportedEncodingException;
    Boolean verifyEmail(String authNumber, String email);
}
