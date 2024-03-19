package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomTypeRepository;
import com.proriberaapp.ribera.Infraestructure.services.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    public Mono<RoomTypeEntity> save(RoomTypeEntity entity) {
        return roomTypeRepository.findByRoomTypeName(entity).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("RoomType already exists"))
                        : roomTypeRepository.save(entity));
    }

    @Override
    public Flux<RoomTypeEntity> saveAll(List<RoomTypeEntity> entity) {
        return roomTypeRepository.findAllByRoomTypeNameIn(entity)
                .collectList()
                .flatMapMany(roomTypeEntities -> roomTypeRepository.saveAll(
                        entity.stream().filter(roomTypeEntity -> !roomTypeEntities.contains(roomTypeEntity)).toList()
                ));
    }

    @Override
    public Mono<RoomTypeEntity> findById(Integer id) {
        return roomTypeRepository.findById(id).
                switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")));
    }

    @Override
    public Flux<RoomTypeEntity> findAll() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")))
                .flatMap(roomTypeRepository::delete);
    }

    @Override
    public Mono<RoomTypeEntity> update(RoomTypeEntity entity) {
        return roomTypeRepository.findById(entity.getRoomTypeId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")))
                .flatMap(roomTypeRepository::save);
    }

}
