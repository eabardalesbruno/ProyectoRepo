package com.proriberaapp.ribera.services.client;



import com.proriberaapp.ribera.Api.controllers.client.dto.WalletTransactionDTO;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletTransactionService {

    Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny , String emailDestiny, String cardNumber, BigDecimal amount , String motiveDescription);
    Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid,  Integer bookingId);
    Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletEntity> findWalletByEmail(String email);
}
