package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
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
    private final RoomRepository roomRepository;
    @Override
    public Mono<RoomEntity> save(RoomEntity roomEntity) {
        return roomRepository.findByRoomName(roomEntity.getRoomName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Room already exists"))
                        : Mono.just(roomEntity))
                .switchIfEmpty(roomRepository.save(roomEntity));
    }

    @Override
    public Flux<RoomEntity> saveAll(Flux<RoomEntity> roomEntity) {
        return roomRepository.findByRoomName(roomEntity)
                .collectList()
                .flatMapMany(roomEntities -> roomRepository.saveAll(
                        roomEntity.filter(
                                roomEntity1 -> !roomEntities.contains(roomEntity1))
                ));
    }

    @Override
    public Mono<RoomEntity> findById(String id) {
        return roomRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return roomRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<RoomEntity> update(RoomEntity roomEntity) {
        return roomRepository.save(roomEntity);
    }
}
