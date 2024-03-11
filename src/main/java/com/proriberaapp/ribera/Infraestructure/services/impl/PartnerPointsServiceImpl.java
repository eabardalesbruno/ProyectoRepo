package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Infraestructure.services.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerPointsServiceImpl implements PartnerPointsService {
    @Override
    public Mono<PartnerPointsEntity> save(PartnerPointsEntity partnerPointsEntity) {
        return null;
    }

    @Override
    public Flux<PartnerPointsEntity> saveAll(Flux<PartnerPointsEntity> partnerPointsEntity) {
        return null;
    }

    @Override
    public Mono<PartnerPointsEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<PartnerPointsEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<PartnerPointsEntity> update(PartnerPointsEntity partnerPointsEntity) {
        return null;
    }
}
