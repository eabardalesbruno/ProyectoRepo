package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Repository
public interface PasswordResetTokenRepository extends R2dbcRepository<PasswordResetTokenEntity, Long> {
    Mono<PasswordResetTokenEntity> findByUserClientIdAndToken(Integer userId, String token);
    Mono<PasswordResetTokenEntity> findByUserClientId(Integer userId);
    Flux<PasswordResetTokenEntity> findByUserClientIdAndTokenAndExpiryDateAfter(Integer userClientId, String token, Timestamp expiryDate);
}