package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.WalletDto;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


public interface WalletService {


  Mono<String> generateUniqueAccountNumber(Integer userId);
  Mono<WalletEntity> createWalletUsuario(Integer userClientId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer userPromoterId, Integer currencyId);
  Mono<WalletEntity> findByUserClientId(Integer userClientId);

  // Mono<WalletEntity> getWallet(Integer walletId);
  // Mono<WalletEntity> topUp(Integer userId, BigDecimal amount);


}
