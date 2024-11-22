package com.proriberaapp.ribera.services.client;



import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletTransactionService {

    Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny , String emailDestiny, String cardNumber, BigDecimal amount , String motiveDescription);
    Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount);
    Mono<WalletEntity> findWalletByEmail(String email);

}
