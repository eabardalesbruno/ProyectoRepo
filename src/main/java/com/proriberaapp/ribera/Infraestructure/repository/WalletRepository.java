package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletRepository extends R2dbcRepository<WalletEntity, Integer> {

    Mono<WalletEntity> findByCardNumber(String cardNumber);
    Mono<WalletEntity> findById (Integer walletId);
    Mono<WalletEntity> findByUserClientId(Integer userClientId);
    Mono<WalletEntity> findByUserPromoterId(Integer userPromoterId);



}
