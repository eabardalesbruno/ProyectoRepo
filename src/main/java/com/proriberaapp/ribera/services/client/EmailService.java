package com.proriberaapp.ribera.services.client;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendEmail(String to, String subject, String body);
    Mono<Void> sendEmailWithAttachment(String toEmail, String body, String subject, String attachment);
    Mono<Void> sendEmailCC(String to, String firstCopia, String secondCopia, String subject, String body);
}
