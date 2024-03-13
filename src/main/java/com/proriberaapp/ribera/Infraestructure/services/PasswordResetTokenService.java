package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PasswordResetTokenRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserRepository;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
<<<<<<< HEAD
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // Importar la clase LoggerFactory
>>>>>>> jose-dev

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenService.class);
    private final PostgresqlConnectionFactory connectionFactory;
    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository, PostgresqlConnectionFactory connectionFactory) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.connectionFactory = connectionFactory;
    }

<<<<<<< HEAD
    public Mono<PasswordResetTokenEntity> generateToken(UserEntity user) {
        String token = generateRandomToken();
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserid(user.getUserId().longValue());
        resetToken.setToken(token);
        resetToken.setExpiryDate(Timestamp.valueOf(LocalDateTime.now().plusMinutes(3))); // Token válido por 3 minutos
        return Mono.just(tokenRepository.save(resetToken));
=======
    public Mono<PasswordResetTokenEntity> generateToken(Integer userId, String token, Timestamp expiryDate) {
        PasswordResetTokenEntity resetToken = new PasswordResetTokenEntity();
        resetToken.setUserId(userId);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);

        return Mono.just(tokenRepository.save(resetToken));
    }

    public Mono<PasswordResetTokenEntity> generateTokenAndSave(Integer userId) {
        String token = generateRandomToken();
        Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusMinutes(3));
        executeInsertQuery(userId, token, expiryDate);
        PasswordResetTokenEntity resetToken = PasswordResetTokenEntity.builder()
                .userId(userId)
                .token(token)
                .passwordstate(0)
                .expiryDate(expiryDate)
                .build();

        logger.info("Consulta de inserción: INSERT INTO passwordresettoken (userid, token, passwordstate, expirydate) " +
                "VALUES (" + userId + ", '" + token + "', 0, '" + expiryDate + "')");

        return Mono.just(tokenRepository.save(resetToken));
    }

    private void executeInsertQuery(Integer userId, String token, Timestamp expiryDate) {
        String insertQuery = "INSERT INTO passwordresettoken (userid, token, passwordstate, expirydate) VALUES (?, ?, ?, ?)";
        connectionFactory.create()
                .flatMapMany(connection -> connection.createStatement(insertQuery)
                        .bind("$1", userId)
                        .bind("$2", token)
                        .bind("$3", 0)
                        .bind("$4", expiryDate)
                        .execute()
                        .flatMap(result -> Mono.just(result))
                ).subscribe();
    }

    public void markTokenAsUsed(Integer userId) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByUserId(userId);
        if (resetToken != null) {
            resetToken.setPasswordstate(1); // Marcar el token como usado
            tokenRepository.save(resetToken);
        }
>>>>>>> jose-dev
    }

    public boolean verifyToken(UserEntity user, String token) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByUserIdAndToken(user.getUserId(), token);
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