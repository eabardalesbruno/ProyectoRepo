package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomService {
    Mono<RoomEntity> save(RoomEntity roomEntity);
    Flux<RoomEntity> saveAll(Flux<RoomEntity> roomEntity);
    Mono<RoomEntity> findById(String id);
    Flux<RoomEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<RoomEntity> update(RoomEntity roomEntity);
}
