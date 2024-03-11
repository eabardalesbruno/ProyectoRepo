package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    @Override
    public Mono<RoomEntity> save(RoomEntity roomEntity) {
        return null;
    }

    @Override
    public Flux<RoomEntity> saveAll(Flux<RoomEntity> roomEntity) {
        return null;
    }

    @Override
    public Mono<RoomEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<RoomEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<RoomEntity> update(RoomEntity roomEntity) {
        return null;
    }
}
