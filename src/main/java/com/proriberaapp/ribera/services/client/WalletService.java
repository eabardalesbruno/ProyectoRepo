package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.payme.dto.AuthorizationResponse;
import com.proriberaapp.ribera.Api.controllers.payme.dto.TransactionNecessaryResponse;
import com.proriberaapp.ribera.Domain.entities.WalletEntity;
import com.proriberaapp.ribera.Domain.entities.WalletTransactionEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletService {


  Mono<String> generateUniqueAccountNumber(Integer userId);

  Mono<WalletEntity> createWalletUsuario(Integer userClientId, Integer currencyId);
  Mono<WalletEntity> createWalletPromoter(Integer userPromoterId, Integer currencyId);



}
