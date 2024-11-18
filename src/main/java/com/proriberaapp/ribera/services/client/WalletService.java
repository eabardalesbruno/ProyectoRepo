package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

public interface WalletService {


  Mono<Integer> generateUniqueAccountNumber(Integer userId);

  Mono<WalletEntity> createWalletUsuario(Integer userId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer promoterId, Integer currencyId);

  Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny, Double amount);
  Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, Double amount);
  Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, Double amount);
  Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, Double amount);
  Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, Double amount);

}
