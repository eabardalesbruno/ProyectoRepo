package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import reactor.core.publisher.Mono;

public interface WalletService {


  Mono<Integer> generateUniqueAccountNumber(Integer userId);

  Mono<WalletEntity> createWalletUsuario(Integer userId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer promoterId, Integer currencyId);


}
