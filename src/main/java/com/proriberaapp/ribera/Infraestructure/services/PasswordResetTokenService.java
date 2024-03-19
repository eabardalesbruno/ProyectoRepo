package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;


@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Mono<PasswordResetTokenEntity> generateToken(UserClientEntity user) {
        String token = generateRandomToken();
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserClientId(user.getUserClientId());
        resetToken.setToken(token);
        resetToken.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(3))); // Token válido por 3 minutos
        return tokenRepository.save(resetToken);
    }

    public Mono<PasswordResetTokenEntity> generateToken(Integer userId, String token, Timestamp expiryDate) {
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserClientId(userId);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);

        return tokenRepository.save(resetToken);
    }

    public Mono<PasswordResetTokenEntity> generateTokenAndSave(Integer userId) {
        String token = generateRandomToken();
        Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusMinutes(3));
        PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
                .userClientId(userId)
                .token(token)
                .passwordState(0)
                .expiryDate(expiryDate)
                .build();

        return tokenRepository.save(resetToken);
    }

    public void markTokenAsUsed(Integer userId) {
        tokenRepository.findByUserClientId(userId)
                .flatMap(resetToken -> {
                    resetToken.setPasswordState(1);
                    return tokenRepository.save(resetToken);
                })
                .subscribe();
    }

    public Mono<Boolean> verifyToken(Integer userId, String token) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return tokenRepository.findByUserClientIdAndTokenAndExpiryDateAfter(userId, token, now)
                .hasElements();
    }
    public PasswordResetTokenEntity saveToken(PasswordResetTokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity).block();
    }

    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}