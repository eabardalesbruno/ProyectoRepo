package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import reactor.core.publisher.Mono;

public interface PartnerPointsService extends BaseService<PartnerPointsEntity,PartnerPointsEntity> {
    Mono<PartnerPointsEntity> incrementPoints(PartnerPointsEntity partnerPointsEntity, Integer increment);
    Mono<PartnerPointsEntity> decrementPoints(PartnerPointsEntity partnerPointsEntity, Integer decrement);
}
