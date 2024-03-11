package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PartnerPointsService {
    Mono<PartnerPointsEntity> save(PartnerPointsEntity partnerPointsEntity);
    Flux<PartnerPointsEntity> saveAll(Flux<PartnerPointsEntity> partnerPointsEntity);
    Mono<PartnerPointsEntity> findById(String id);
    Flux<PartnerPointsEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<PartnerPointsEntity> update(PartnerPointsEntity partnerPointsEntity);
}
