package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CurrencyTypeEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyTypeRepository extends R2dbcRepository<CurrencyTypeEntity, Integer> {
    Mono<CurrencyTypeEntity> findByCode(String code);

    Mono<CurrencyTypeEntity> findByCurrencyTypeName(String currencyTypeName);
}
