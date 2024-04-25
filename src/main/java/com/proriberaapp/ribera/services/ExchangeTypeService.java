package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.ExchangeTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeTypeService {
    Mono<ExchangeTypeEntity> createExchangeType(ExchangeTypeEntity exchangeType);
    Mono<ExchangeTypeEntity> getExchangeTypeById(Integer id);
    Flux<ExchangeTypeEntity> getAllExchangeTypes();
    Mono<ExchangeTypeEntity> updateExchangeType(Integer id, ExchangeTypeEntity exchangeType);
    Mono<Void> deleteExchangeType(Integer id);
}