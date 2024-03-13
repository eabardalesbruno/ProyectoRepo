package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
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

    public Mono<PasswordResetTokenEntity> generateToken(UserEntity user) {
        String token = generateRandomToken();
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserid(user.getUserId().longValue());
        resetToken.setToken(token);
        resetToken.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(3))); // Token válido por 3 minutos
        return Mono.just(tokenRepository.save(resetToken));
    }

    public boolean verifyToken(UserEntity user, String token) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByUserAndToken(user.getUserId(), token);
        return resetToken != null && resetToken.getExpiryDate().after(Timestamp.valueOf(LocalDateTime.now()));
    }
    public PasswordResetTokenEntity saveToken(PasswordResetTokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
    }
    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}