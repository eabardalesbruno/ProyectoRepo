package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.TokenPointsTransaction;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenPointsTransactionRepository extends R2dbcRepository<TokenPointsTransaction, Integer> {
    Mono<TokenPointsTransaction> findByCodigoToken(String codigoToken);
}