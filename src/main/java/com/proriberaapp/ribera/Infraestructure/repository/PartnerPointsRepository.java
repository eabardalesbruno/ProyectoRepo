package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface PartnerPointsRepository extends R2dbcRepository<PartnerPointsEntity, Integer> {
    Mono<PartnerPointsEntity> findByPartnerPointIdAndUserClientId(Integer partnerPointId, Integer userClientId);
    Mono<PartnerPointsEntity> findByUserClientId(Integer userClientId);
}
