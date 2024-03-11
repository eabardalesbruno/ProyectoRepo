package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomDetailService {
    Mono<RoomDetailEntity> save(RoomDetailEntity roomDetailEntity);
    Flux<RoomDetailEntity> saveAll(Flux<RoomDetailEntity> roomDetailEntity);
    Mono<RoomDetailEntity> findById(String id);
    Flux<RoomDetailEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<RoomDetailEntity> update(RoomDetailEntity roomDetailEntity);
}
