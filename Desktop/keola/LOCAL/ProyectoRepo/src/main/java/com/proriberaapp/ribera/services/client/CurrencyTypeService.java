package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyTypeService {
    Mono<CurrencyTypeEntity> createCurrencyType(CurrencyTypeEntity currencyType);

    Mono<CurrencyTypeEntity> getCurrencyType(Integer currencyTypeId);

    Flux<CurrencyTypeEntity> getAllCurrencyTypes();

    Mono<Void> deleteCurrencyType(Integer currencyTypeId);
}
