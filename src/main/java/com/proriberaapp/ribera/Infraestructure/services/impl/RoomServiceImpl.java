package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
import com.proriberaapp.ribera.Infraestructure.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                        : roomRepository.save(roomEntity));
    }

    @Override
    public Flux<RoomEntity> saveAll(List<RoomEntity> roomEntity) {
        return roomRepository.findAllByRoomNameIn(roomEntity.stream().map(RoomEntity::getRoomName).toList())
                .collectList()
                .flatMapMany(roomEntities -> roomRepository.saveAll(
                        roomEntity.stream().filter(roomEntity1 -> !roomEntities.contains(roomEntity1)).toList()
                ));
    }

    @Override
    public Mono<RoomEntity> findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Override
    public Flux<RoomEntity> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomRepository.deleteById(id);
    }

    @Override
    public Mono<RoomEntity> update(RoomEntity roomEntity) {
        return roomRepository.save(roomEntity);
    }
}
