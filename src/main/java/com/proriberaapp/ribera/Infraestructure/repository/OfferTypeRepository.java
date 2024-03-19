package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OfferTypeRepository extends R2dbcRepository<OfferTypeEntity, Integer> {
    Mono<OfferTypeEntity> findByOfferTypeName(OfferTypeEntity entity);

    Flux<OfferTypeEntity> findAllByOfferTypeNameIn(List<OfferTypeEntity> entity);
}
