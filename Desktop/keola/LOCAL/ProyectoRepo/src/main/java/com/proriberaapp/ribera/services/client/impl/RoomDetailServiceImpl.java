package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomDetailServiceImpl implements com.proriberaapp.ribera.services.client.RoomDetailService {
    private final RoomDetailRepository roomDetailRepository;
    @Override
    public Mono<RoomDetailEntity> save(RoomDetailEntity roomDetailEntity) {
        return roomDetailRepository.save(roomDetailEntity);
    }

    @Override
    public Flux<RoomDetailEntity> saveAll(List<RoomDetailEntity> roomDetailEntity) {
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
