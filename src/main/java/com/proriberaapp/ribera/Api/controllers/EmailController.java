package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.dto.EmailRequest;
import com.proriberaapp.ribera.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody())
                .thenReturn(ResponseEntity.ok("Email enviado"))
                .onErrorReturn(ResponseEntity.status(500).body("Enviado"));
    }
}