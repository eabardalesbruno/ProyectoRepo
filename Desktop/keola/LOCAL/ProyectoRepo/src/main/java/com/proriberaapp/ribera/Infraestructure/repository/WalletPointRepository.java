package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.WalletPointEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface WalletPointRepository extends ReactiveCrudRepository<WalletPointEntity, Integer> {
    Mono<WalletPointEntity> findByUserId(Integer userId);

    @Query("SELECT * FROM wallet_point wp LEFT JOIN userclient u ON u.userclientid  = wp.userId WHERE u.username = :username")
    Mono<WalletPointEntity> findByUsername(String username);

    @Query("SELECT * FROM wallet_point wp LEFT JOIN userclient u ON u.userclientid  = wp.userId WHERE u.email = :email")
    Mono<WalletPointEntity> findByEmail(String email);

}

