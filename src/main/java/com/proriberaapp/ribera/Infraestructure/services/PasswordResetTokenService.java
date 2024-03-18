package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
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
        return Mono.just(tokenRepository.save(resetToken));
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
<<<<<<< HEAD
        String selectQuery = "SELECT COUNT(*) FROM passwordresettoken WHERE userid = " + userId +
                " AND token = '" + token + "' AND expirydate > '" + Timestamp.valueOf(LocalDateTime.now()) + "'";

        return connectionFactory.create()
                .flatMapMany(connection -> connection.createStatement(selectQuery)
                        .execute()
                        .flatMap(result -> result.map((row, rowMetadata) -> row.get(0, Integer.class)))
                        .map(count -> count == 1)
                )
<<<<<<< HEAD
                .next(); // Convertir Flux<Boolean> a Mono<Boolean>
<<<<<<< HEAD
    }
    public PasswordResetTokenEntity saveToken(PasswordResetTokenEntity tokenEntity) {
        return tokenRepository.save(tokenEntity);
=======
>>>>>>> jose-dev
=======
                .next();
>>>>>>> jose-dev
=======
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        return tokenRepository.findByUserClientIdAndTokenAndExpiryDateAfter(userId, token, now)
                .hasElements();
>>>>>>> jose-dev
    }

    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}