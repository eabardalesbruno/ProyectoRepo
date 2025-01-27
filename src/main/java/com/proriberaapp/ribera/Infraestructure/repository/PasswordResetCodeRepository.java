package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.PasswordResetCodeEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

@Repository
public interface PasswordResetCodeRepository extends R2dbcRepository<PasswordResetCodeEntity, Integer> {
    @Query("""
             SELECT * FROM password_reset_codes
            WHERE reset_code = :code
            """)
    Mono<PasswordResetCodeEntity> findByCode(String code);
}
