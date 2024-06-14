package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PartnerPointsService extends BaseService<PartnerPointsEntity,PartnerPointsEntity> {
    Mono<PartnerPointsEntity> incrementPoints(PartnerPointsEntity partnerPointsEntity, Integer increment);
    Mono<PartnerPointsEntity> decrementPoints(PartnerPointsEntity partnerPointsEntity, Integer decrement);
    Mono<PartnerPointsEntity> paymentPoints(Integer userClientId, Integer pointsBuy, BigDecimal amountFinal);

    Mono<PartnerPointsEntity> getPartnerPointsByUserClientId(Integer userClientId);
}
