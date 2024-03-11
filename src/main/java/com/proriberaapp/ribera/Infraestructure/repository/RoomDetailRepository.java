package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomDetailRepository extends R2dbcRepository<RoomDetailEntity, Integer> {
    Mono<RoomDetailEntity> findByRoomDetailIdAndRoomId(Integer roomDetailId, Integer roomId);
    Flux<RoomDetailEntity> findByRoomDetailIdAndRoomId(Flux<RoomDetailEntity> roomDetailId, Flux<Integer> roomId);
}
