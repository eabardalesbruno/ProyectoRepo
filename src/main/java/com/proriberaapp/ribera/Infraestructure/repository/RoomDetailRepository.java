package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomDetailRepository extends R2dbcRepository<RoomDetailEntity, Integer> {
    Mono<RoomDetailEntity> findByRoomName(String roomName);
    Flux<RoomDetailEntity> findByRoomName(Flux<RoomDetailEntity> roomName);

    Mono<Object> findByIdAndRoomId(Integer roomDetailId, Integer roomId);
}
