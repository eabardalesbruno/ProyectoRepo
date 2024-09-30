package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomStateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements com.proriberaapp.ribera.services.client.RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final RoomStateRepository roomStateRepository;

    @Override
    public Mono<RoomTypeEntity> save(RoomTypeEntity entity) {
        return roomTypeRepository.findByRoomTypeName(entity.getRoomTypeName())
                .flatMap(existing -> Mono.error(new IllegalArgumentException("RoomType already exists")))
                .then(roomTypeRepository.save(entity));
    }

    @Override
    public Flux<RoomTypeEntity> saveAll(List<RoomTypeEntity> entities) {
        return roomTypeRepository.findAllByRoomTypeNameIn(
                        entities.stream().map(RoomTypeEntity::getRoomTypeName).toList())
                .collectList()
                .flatMapMany(existingEntities -> roomTypeRepository.saveAll(
                        entities.stream().filter(entity -> !existingEntities.stream()
                                        .anyMatch(existing -> existing.getRoomTypeName().equals(entity.getRoomTypeName())))
                                .toList()
                ));
    }

    @Override
    public Mono<RoomTypeEntity> findById(Integer id) {
        return roomTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")));
    }

    @Override
    public Flux<RoomTypeEntity> findAll() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return roomRepository.findAllByRoomTypeId(id)
                .collectList()
                .flatMap(rooms -> {
                    if (rooms.isEmpty()) {
                        return roomTypeRepository.findById(id)
                                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontro el tipo de habitacion")))
                                .flatMap(roomTypeRepository::delete);
                    } else {
                        return Mono.error(new IllegalArgumentException("No se puede eliminar el tipo de habitacion porque tiene alojamientos asociadas"));
                    }
                });
    }

    @Override
    public Flux<RoomTypeEntity> getAllRoomTypes() {
        return roomTypeRepository.findAll()
                .flatMap(roomTypeEntity -> roomStateRepository.findById(roomTypeEntity.getRoomstateid())
                        .map(roomState -> {
                            roomTypeEntity.setRoomState(roomState);
                            return roomTypeEntity;
                        })
                );
    }

    @Override
    public Mono<RoomTypeEntity> findByRoomTypeId(Integer id) {
        return roomTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")));
    }

    @Override
    public Mono<RoomTypeEntity> update(RoomTypeEntity entity) {
        return roomTypeRepository.findById(entity.getRoomTypeId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("RoomType not found")))
                .flatMap(existingRoomType -> {
                    existingRoomType.setRoomType(entity.getRoomType());
                    existingRoomType.setRoomTypeName(entity.getRoomTypeName());
                    existingRoomType.setRoomTypeDescription(entity.getRoomTypeDescription());
                    existingRoomType.setRoomstateid(entity.getRoomstateid());
                    return roomTypeRepository.save(existingRoomType);
                });
    }

    @Override
    public Mono<RoomTypeEntity> createRoomType(RoomTypeEntity roomTypeEntity) {
        return save(roomTypeEntity);
    }

    @Override
    public Mono<RoomTypeEntity> updateRoomType(Integer id, RoomTypeEntity roomTypeEntity) {
        roomTypeEntity.setRoomTypeId(id);
        return update(roomTypeEntity);
    }

    @Override
    public Mono<Void> deleteRoomType(Integer id) {
        return deleteById(id);
    }
}
