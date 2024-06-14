package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.EmailLogEntity;
import com.proriberaapp.ribera.Infraestructure.repository.EmailLogRepository;
import com.proriberaapp.ribera.services.client.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public Mono<Void> sendEmail(String to, String subject, String body) {
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("inclubnotification@inclub.world");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(body, true); // true indica que el cuerpo es HTML
                mailSender.send(message);

                EmailLogEntity emailLog = EmailLogEntity.builder()
                        .recipient(to)
                        .subject(subject)
                        .body(body)
                        .sentDate(Timestamp.valueOf(LocalDateTime.now()))
                        .status("SENT")
                        .build();

                emailLogRepository.save(emailLog).block();
            } catch (MessagingException e) {
                EmailLogEntity emailLog = EmailLogEntity.builder()
                        .recipient(to)
                        .subject(subject)
                        .body(body)
                        .sentDate(Timestamp.valueOf(LocalDateTime.now()))
                        .status("FAILED")
                        .build();

                emailLogRepository.save(emailLog).block();
            }
        }).then();
    }
}

/*
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, EmailLogRepository emailLogRepository) {
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public Mono<Void> sendEmail(String to, String subject, String body) {
        return Mono.fromRunnable(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("inclubnotification@inclub.world");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);

                EmailLogEntity emailLog = EmailLogEntity.builder()
                        .recipient(to)
                        .subject(subject)
                        .body(body)
                        .sentDate(Timestamp.valueOf(LocalDateTime.now()))
                        .status("SENT")
                        .build();

                emailLogRepository.save(emailLog).block();
            } catch (Exception e) {
                EmailLogEntity emailLog = EmailLogEntity.builder()
                        .recipient(to)
                        .subject(subject)
                        .body(body)
                        .sentDate(Timestamp.valueOf(LocalDateTime.now()))
                        .status("FAILED")
                        .build();

                emailLogRepository.save(emailLog).block();
            }
        }).then();
    }
}
 */