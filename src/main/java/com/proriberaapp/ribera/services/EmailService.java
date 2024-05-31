package com.proriberaapp.ribera.services;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendEmail(String to, String subject, String body);
}
