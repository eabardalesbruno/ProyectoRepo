package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BedroomService extends BaseService<BedroomEntity, BedroomEntity> {
    Mono<Void> deleteByRoomId(Integer roomId);

    Flux<BedroomEntity> findByRoomId(Integer roomId);

    Mono<Integer> countByBedTypeId(Integer bedTypeId);
}
