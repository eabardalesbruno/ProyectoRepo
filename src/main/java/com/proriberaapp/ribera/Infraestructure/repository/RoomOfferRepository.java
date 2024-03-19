package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomOfferRepository extends R2dbcRepository<RoomOfferEntity, Integer> {
    Mono<RoomOfferEntity> findByRoomIdAndOfferTypeId(Integer roomId, Integer offerTypeId);

    Flux<RoomOfferEntity> findAllByRoomIdInAndOfferTypeIdIn(List<RoomOfferEntity> roomOfferEntity,List<RoomOfferEntity> roomOfferEntity1);
}
