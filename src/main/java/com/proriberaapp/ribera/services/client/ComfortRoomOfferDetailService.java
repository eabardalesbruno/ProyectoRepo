package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ComfortRoomOfferDetailService extends BaseService<ComfortRoomOfferDetailEntity, ComfortRoomOfferDetailEntity> {
    Flux<ComfortRoomOfferDetailEntity> findAllByRoomOfferId(Integer roomOfferId);

    Mono<Integer> countByComfortTypeId(Integer comfortTypeId);
}
