package com.proriberaapp.ribera.services.client;


import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

public interface WalletTransactionService {

    Mono<WalletTransactionEntity> createWalletTransaction(WalletTransactionEntity walletTransaction);

}
