package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomDetailRepository;
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
    private final RoomDetailRepository roomDetailRepository;
    @Override
    public Mono<RoomDetailEntity> save(RoomDetailEntity roomDetailEntity) {
        return roomDetailRepository.save(roomDetailEntity);
    }

    @Override
    public Flux<RoomDetailEntity> saveAll(Flux<RoomDetailEntity> roomDetailEntity) {
        return roomDetailRepository.saveAll(roomDetailEntity);
    }

    @Override
    public Mono<RoomDetailEntity> findById(Integer id) {
        return roomDetailRepository.findById(id);
    }

    @Override
    public Flux<RoomDetailEntity> findAll() {
        return roomDetailRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomDetailRepository.deleteById(id);
    }

    @Override
    public Mono<RoomDetailEntity> update(RoomDetailEntity roomDetailEntity) {
        return roomDetailRepository.save(roomDetailEntity);
    }
}
