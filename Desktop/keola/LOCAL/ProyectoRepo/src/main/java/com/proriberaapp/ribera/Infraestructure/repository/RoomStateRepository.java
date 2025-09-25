package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RoomStateRepository extends R2dbcRepository<RoomStateEntity, Integer> {
    Mono<RoomStateEntity> findByRoomStateName(String roomStateName);
}
