package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.dto.EmailRequest;
import com.proriberaapp.ribera.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public Mono<Void> sendEmail(@RequestBody EmailRequest request) {
        return emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
    }
}