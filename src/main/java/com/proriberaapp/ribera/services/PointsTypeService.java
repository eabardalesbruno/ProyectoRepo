package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PointsTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PointsTypeService {
    Mono<PointsTypeEntity> createPointsType(PointsTypeEntity pointsType);
    Mono<PointsTypeEntity> getPointsTypeById(Integer id);
    Flux<PointsTypeEntity> getAllPointsTypes();
    Mono<PointsTypeEntity> updatePointsType(Integer id, PointsTypeEntity pointsType);
    Mono<Void> deletePointsType(Integer id);
}