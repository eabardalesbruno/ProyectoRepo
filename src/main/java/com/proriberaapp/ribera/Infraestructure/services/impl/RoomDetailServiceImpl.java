package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomDetailServiceImpl implements RoomDetailService {
    @Override
    public Mono<RoomDetailEntity> save(RoomDetailEntity roomDetailEntity) {
        return null;
    }

    @Override
    public Flux<RoomDetailEntity> saveAll(Flux<RoomDetailEntity> roomDetailEntity) {
        return null;
    }

    @Override
    public Mono<RoomDetailEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<RoomDetailEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<RoomDetailEntity> update(RoomDetailEntity roomDetailEntity) {
        return null;
    }
}
