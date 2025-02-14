package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import reactor.core.publisher.Mono;


public interface WalletService {


  Mono<String> generateUniqueAccountNumber(Integer userId);
  Mono<WalletEntity> createWalletUsuario(Integer userClientId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer userPromoterId, Integer currencyId);




}
