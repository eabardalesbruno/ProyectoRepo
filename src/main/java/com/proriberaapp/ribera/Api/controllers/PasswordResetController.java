package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.PasswordResetTokenService;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @PostMapping("/request")
    public Mono<ResponseEntity<String>> requestPasswordReset(@RequestParam String email) {
        return passwordResetTokenService.generateAndSendToken(email);
    }

    @PostMapping("/verify")
    public Mono<ResponseEntity<String>> verifyPasswordReset(@RequestParam String email, @RequestParam String code) {
        return userService.findByEmail(email)
                .flatMap(user -> {
                    boolean isValid = passwordResetTokenService.verifyToken(user, code);
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok("El código es válido"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código no es válido"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado")));
    }

    @PostMapping("/confirm")
    public Mono<ResponseEntity<String>> confirmPasswordReset(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        return userService.findByEmail(email)
                .flatMap(user -> {
                    boolean isValid = passwordResetTokenService.verifyToken(user, code);
                    if (isValid) {
                        userService.updatePassword(user, newPassword);
                        return Mono.just(ResponseEntity.ok("Su nueva contraseña ha sido generada exitosamente"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código no es válido"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado")));
    }
}