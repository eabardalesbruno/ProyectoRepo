package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.WalletTransactionDTO;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WalletTransactionRepository extends R2dbcRepository<WalletTransactionEntity,Integer> {
    Mono<WalletTransactionEntity> findByOperationCode(String operationCode);

    @Query("""
        SELECT 
            COALESCE(u.firstName || ' ' || u.lastName, p.firstName || ' ' || p.lastName) AS username,
            wt.inicialdate, 
            wt.amount, 
            wt.description, 
            wt.operationcode
        FROM wallettransaction wt
        LEFT JOIN userclient u ON wt.walletid = u.walletid
        LEFT JOIN userpromoter p ON wt.walletid = p.walletid
        WHERE wt.walletid = :walletId
    """)
    Flux<WalletTransactionDTO> findTransactionDetailsByWalletId(Integer walletId);

}
