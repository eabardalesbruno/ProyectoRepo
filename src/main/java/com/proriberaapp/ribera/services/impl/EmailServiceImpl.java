package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Api.controllers.dto.EmailRequest;
import com.proriberaapp.ribera.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.extra.processor.TopicProcessor;

@Service
public class EmailServiceImpl implements EmailService {

    private final TopicProcessor<EmailRequest> emailProcessor;

    @Autowired
    public EmailServiceImpl() {
        this.emailProcessor = TopicProcessor.<EmailRequest>builder().name("emailProcessor").build();
        this.emailProcessor.subscribeOn(Schedulers.parallel())
                .map(this::sendEmail)
                .subscribe();
    }

    @Override
    public Mono<Void> sendEmail(String to, String subject, String body) {
        return Mono.fromRunnable(() -> emailProcessor.onNext(new EmailRequest(to, subject, body)));
    }

    private Mono<Void> sendEmail(EmailRequest request) {
        // Lógica para enviar el correo electrónico
        System.out.println("Sending email to: " + request.getTo());
        System.out.println("Subject: " + request.getSubject());
        System.out.println("Body: " + request.getBody());
        return Mono.empty();
    }
}