package com.proriberaapp.ribera.services.point;

import org.apache.poi.ss.formula.functions.T;

import com.proriberaapp.ribera.Domain.entities.PointTransactionEntity;

import reactor.core.publisher.Mono;

public interface PointsTransactionStrategy<T extends PointTransactionRequestDto> {
    Mono<T> execute(T request);
}