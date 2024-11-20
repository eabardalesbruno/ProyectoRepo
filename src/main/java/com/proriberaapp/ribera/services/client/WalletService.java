package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletService {


  Mono<Integer> generateUniqueAccountNumber(Integer userId);

  Mono<WalletEntity> createWalletUsuario(Integer userId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer promoterId, Integer currencyId);

  Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny ,String emailDestiny, String nameDestiny, BigDecimal amount);
  Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount);


}
