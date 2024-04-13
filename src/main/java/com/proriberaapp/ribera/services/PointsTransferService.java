package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PointsTransferEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PointsTransferService {
    Mono<PointsTransferEntity> createPointsTransfer(PointsTransferEntity pointsTransfer);
    Mono<PointsTransferEntity> getPointsTransferById(Integer id);
    Flux<PointsTransferEntity> getAllPointsTransfers();
    Mono<PointsTransferEntity> updatePointsTransfer(Integer id, PointsTransferEntity pointsTransfer);
    Mono<Void> deletePointsTransfer(Integer id);
}