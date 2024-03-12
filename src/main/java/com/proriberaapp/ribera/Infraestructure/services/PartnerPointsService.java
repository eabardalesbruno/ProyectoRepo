package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PartnerPointsService {
    Mono<PartnerPointsEntity> save(PartnerPointsEntity partnerPointsEntity);
    Mono<PartnerPointsEntity> findById(String id);
    Flux<PartnerPointsEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<PartnerPointsEntity> update(PartnerPointsEntity partnerPointsEntity);
}
