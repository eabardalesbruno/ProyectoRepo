package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PartnerPointsRepository extends R2dbcRepository<PartnerPointsEntity, Integer> {
    Mono<Object> findByPartnerPointIdAndUserId(Integer partnerPointId, Integer userId);
    Flux<Object> findByPartnerPointIdAndUserId(Flux<Integer> partnerPointId, Flux<Integer> userId);
}
