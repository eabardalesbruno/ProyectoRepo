package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public Mono<ResponseEntity<String>> generateAndSendToken(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    String token = generateRandomToken();
                    PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
                    resetToken.setUserid(user.getUserId());
                    resetToken.setToken(token);
                    resetToken.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(3)));
                    return Mono.just(tokenRepository.save(resetToken))
                            .map(savedToken -> {
                                return ResponseEntity.ok("Token generado y enviado al correo del usuario.");
                            });
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuario con el correo proporcionado no encontrado.")));
    }

    public boolean verifyToken(UserEntity user, String token) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByUserIdAndToken(user.getUserId(), token);
        return resetToken != null && resetToken.getExpiryDate().after(Timestamp.valueOf(LocalDateTime.now()));
    }

    public void saveToken(PasswordResetTokenEntity tokenEntity) {
        tokenRepository.save(tokenEntity);
    }

    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}