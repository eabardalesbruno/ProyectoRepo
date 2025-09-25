package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.WithdrawalRequestEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WithdrawalRequestRepository extends R2dbcRepository<WithdrawalRequestEntity, Integer> {
    
    Mono<WithdrawalRequestEntity> findByOperationNumber(String operationNumber);
    
    Flux<WithdrawalRequestEntity> findByUserId(Integer userId);
    
    Flux<WithdrawalRequestEntity> findByWalletId(Integer walletId);
    
    Flux<WithdrawalRequestEntity> findByStatus(String status);
    
    @Query("SELECT * FROM withdrawal_requests WHERE status = :status ORDER BY creation_date DESC")
    Flux<WithdrawalRequestEntity> findAllByStatusOrderByCreationDateDesc(String status);
    
    @Query("SELECT * FROM withdrawal_requests ORDER BY creation_date DESC")
    Flux<WithdrawalRequestEntity> findAllOrderByCreationDateDesc();
} 