package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletService {


  Mono<String> generateUniqueAccountNumber(Integer userId);

  Mono<WalletEntity> createWalletUsuario(Integer userClientId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer userPromoterId, Integer currencyId);

  Mono<WalletTransactionEntity> makeTransfer (Integer walletIdOrigin, Integer walletIdDestiny ,String emailDestiny, String documentNumber, BigDecimal amount);
  Mono<WalletTransactionEntity> makeWithdrawal(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makeDeposit(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makePayment(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletTransactionEntity> makeRecharge(Integer walletId, Integer transactioncatid, BigDecimal amount);
  Mono<WalletEntity> findWalletByEmailOrDocument(String email, String documentNumber);


}
