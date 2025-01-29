package com.proriberaapp.ribera.Api.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proriberaapp.ribera.Domain.entities.PasswordResetCodeEntity;
import com.proriberaapp.ribera.services.client.PasswordResetCodeService;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetCodeController {
    @Autowired
    private PasswordResetCodeService passwordResetCodeService;

    @GetMapping("/validate/{code}")
    public Mono<PasswordResetCodeEntity> getMethodName(@PathVariable String code) {
        return this.passwordResetCodeService.verfiedCode(code);
    }

}
