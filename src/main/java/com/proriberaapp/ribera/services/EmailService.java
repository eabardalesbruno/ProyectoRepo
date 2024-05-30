package com.proriberaapp.ribera.services;
import reactor.core.publisher.Mono;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
