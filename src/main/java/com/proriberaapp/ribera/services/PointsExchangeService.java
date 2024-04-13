package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PointsExchangeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PointsExchangeService {
    Mono<PointsExchangeEntity> createPointsExchange(PointsExchangeEntity pointsExchange);
    Mono<PointsExchangeEntity> getPointsExchangeById(Integer id);
    Flux<PointsExchangeEntity> getAllPointsExchanges();
    Mono<PointsExchangeEntity> updatePointsExchange(Integer id, PointsExchangeEntity pointsExchange);
    Mono<Void> deletePointsExchange(Integer id);
}