package com.proriberaapp.ribera.services.client;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendEmail(String to, String subject, String body);
}
