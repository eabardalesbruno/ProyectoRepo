package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PartnerPointsRepository extends R2dbcRepository<PartnerPointsEntity, Integer> {
    Mono<Object> findByPartnerPointIdAndUserClientId(Integer partnerPointId, Integer userClientId);
    Flux<Object> findByPartnerPointIdAndUserClientId(Flux<Integer> partnerPointId, Flux<Integer> userClientId);
}
