package com.example.santa.domain.mail.service;

import com.example.santa.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@PropertySource("classpath:application.properties")
@Service
@RequiredArgsConstructor
public class EmailSendServiceImpl implements EmailSendService {
    private final JavaMailSender javaMailSender;
    // 인증번호 생성
    private String authNumber;

    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String username;

    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        createKey();
        log.info("보내는 대상: {}", to);
        log.info("인증 번호: {}", authNumber);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // to 보내는 대상
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, to);
        // 메일 제목
        mimeMessage.setSubject("산타 이메일 인증 코드: ");

        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 인증코드 칸에 입력해 주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += authNumber;
        msg += "</td></tr></tbody></table></div>";

        //내용, charset 타입, subtype
        mimeMessage.setText(msg, "utf-8", "html");
        //보내는 사람의 메일 주소, 보내는 사람 이름
        mimeMessage.setFrom(new InternetAddress(username, "santa_Admin"));
        return mimeMessage;
    }


    @Override
    public String sendSimpleMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = createMessage(to);
        try {
            // 유효시간 1분
            redisUtil.setDataExpire(authNumber, to, 60 * 1L);
            javaMailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
        }
        return authNumber;
    }

    @Override
    public Boolean verifyEmail(String authNumber, String email) {
        String memberEmail = redisUtil.getData(authNumber);
        if (memberEmail == null) {
            return false;
        } else if (!memberEmail.equals(email)) {
            return false;
        }
        redisUtil.deleteData(authNumber);
        return true;
    }

    public void createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            key.append((random.nextInt(10)));
        }
        this.authNumber = key.toString();
    }
}


