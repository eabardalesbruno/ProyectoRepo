package com.proriberaapp.ribera.services.client;
import com.proriberaapp.ribera.Domain.entities.RoomStateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomStateService {
    Mono<RoomStateEntity> createRoomState(RoomStateEntity roomStateEntity);
    Mono<RoomStateEntity> updateRoomState(Integer id, RoomStateEntity roomStateEntity);
    Mono<Void> deleteRoomState(Integer id);
    Flux<RoomStateEntity> getAllRoomStates();
}