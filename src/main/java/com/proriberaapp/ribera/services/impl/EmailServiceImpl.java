package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.EmailLogEntity;
import com.proriberaapp.ribera.Infraestructure.repository.EmailLogRepository;
import com.proriberaapp.ribera.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
                SimpleMailMessage message = new SimpleMailMessage();
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

                emailLogRepository.save(emailLog).block();  // Usa block para asegurarte de que la operación de guardado se complete
            } catch (Exception e) {
                EmailLogEntity emailLog = EmailLogEntity.builder()
                        .recipient(to)
                        .subject(subject)
                        .body(body)
                        .sentDate(Timestamp.valueOf(LocalDateTime.now()))
                        .status("FAILED")
                        .build();

                emailLogRepository.save(emailLog).block();  // Usa block para asegurarte de que la operación de guardado se complete
            }
        }).then();
    }
}