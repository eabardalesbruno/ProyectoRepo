package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserClientEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;


@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenService.class);
    private final PostgresqlConnectionFactory connectionFactory;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository, PostgresqlConnectionFactory connectionFactory) {
        this.tokenRepository = tokenRepository;
        this.connectionFactory = connectionFactory;
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
        executeInsertQuery(userId, token, expiryDate);
        PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
                .userClientId(userId)
                .token(token)
                .passwordState(0)
                .expiryDate(expiryDate)
                .build();

        logger.info("Consulta de inserción: INSERT INTO passwordresettoken (userid, token, passwordstate, expirydate) " +
                "VALUES (" + userId + ", '" + token + "', 0, '" + expiryDate + "')");

        return tokenRepository.save(resetToken);
    }

    private void executeInsertQuery(Integer userId, String token, Timestamp expiryDate) {
        String insertQuery = "INSERT INTO passwordresettoken (userclientid, token, passwordstate, expirydate) VALUES ("
                + userId + ", '" + token + "', 0, '" + expiryDate + "')";

        connectionFactory.create()
                .flatMapMany(connection -> connection.createStatement(insertQuery)
                        .execute()
                        .flatMap(result -> result.map((row, rowMetadata) -> row.get(0, Integer.class)))
                )
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated == 1) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new RuntimeException("Error al insertar en la tabla passwordresettoken"));
                    }
                })
                .subscribe(
                        success -> logger.info("Inserción en la tabla passwordresettoken exitosa"),
                        error -> {
                            logger.error("Error al insertar en la tabla passwordresettoken: " + error.getMessage());
                            error.printStackTrace();
                        }
                );
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
                .hasElements(); // Verifica si hay al menos un elemento en el Flux
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