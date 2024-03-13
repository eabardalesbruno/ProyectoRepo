package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Infraestructure.services.PasswordResetTokenService;
import com.proriberaapp.ribera.Infraestructure.services.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetController {

    private final UserClientService userClientService;
    private final PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public PasswordResetController(UserClientService userClientService, PasswordResetTokenService passwordResetTokenService) {
        this.userClientService = userClientService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    /*
    @PostMapping("/request")
    public Mono<ResponseEntity<Map<String, Object>>> requestPasswordReset(@RequestParam String email) {
        return userService.findByEmail(email)
<<<<<<< HEAD
                .flatMap(user -> passwordResetTokenService.generateToken(user)
                        .map(token -> {
                            return ResponseEntity.ok("Enviamos a su correo el código");
                        })
                        .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo generar el token"))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado")));
    }
     */
    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }
    @PostMapping("/request")
    public Mono<ResponseEntity<String>> requestPasswordReset(@RequestParam String email) {
        return userService.findByEmail(email)
                .flatMap(user -> passwordResetTokenService.generateToken(user)
                        .map(token -> ResponseEntity.ok("Enviamos a su correo el código"))
                        .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo generar el token")))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado")));
=======
                .flatMap(user -> {
                    String token = generateRandomToken();
                    Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusMinutes(3)); // Ajustar según sea necesario

                    return passwordResetTokenService.generateToken(user.getUserId(), token, expiryDate)
                            .map(resetToken -> {
                                Map<String, Object> responseMap = new HashMap<>();
                                responseMap.put("token", resetToken.getToken());
                                responseMap.put("userId", resetToken.getUserid());
                                return ResponseEntity.ok(responseMap);
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"))));
    }

     */

    @PostMapping("/request")
    public Mono<ResponseEntity<Map<String, Object>>> requestPasswordReset(@RequestParam String email) {
        return userClientService.findByEmail(email)
                .flatMap(user -> passwordResetTokenService.generateTokenAndSave(user.getUserClientId())
                        .map(resetToken -> {
                            Map<String, Object> responseMap = new HashMap<>();
                            responseMap.put("token", resetToken.getToken());
                            responseMap.put("userId", resetToken.getUserClientId());
                            return ResponseEntity.ok(responseMap);
                        }))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Usuario no encontrado"))));
    }

    // Method to generate a random 6-digit token
    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
>>>>>>> jose-dev
    }

    @PostMapping("/verify")
    public Mono<ResponseEntity<String>> verifyPasswordReset(@RequestParam String email, @RequestParam String code) {
        return userClientService.findByEmail(email)
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
        return userClientService.findByEmail(email)
                .flatMap(user -> {
                    boolean isValid = passwordResetTokenService.verifyToken(user, code);
                    if (isValid) {
                        userClientService.updatePassword(user, newPassword);
                        passwordResetTokenService.markTokenAsUsed(user.getUserClientId());
                        return Mono.just(ResponseEntity.ok("Su nueva contraseña ha sido generada exitosamente"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El código no es válido"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado")));
    }
}