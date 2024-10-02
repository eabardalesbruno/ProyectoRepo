package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RoomRepository;
import com.proriberaapp.ribera.services.client.RoomService;
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

    @Override
    public Mono<RoomEntity> createRoom(RoomEntity room) {
        return roomRepository.save(room);
    }

    @Override
    public Mono<RoomEntity> updateRoom(Integer roomId, RoomEntity room) {
        return roomRepository.findById(roomId)
                .flatMap(existingRoom -> {
                    existingRoom.setRoomName(room.getRoomName());
                    existingRoom.setRoomTypeId(room.getRoomTypeId());
                    existingRoom.setStateRoomId(room.getStateRoomId());
                    existingRoom.setRoomDetailId(room.getRoomDetailId());
                    existingRoom.setRoomDescription(room.getRoomDescription());
                    existingRoom.setRoomNumber(room.getRoomNumber());
                    existingRoom.setCapacity(room.getCapacity());
                    return roomRepository.save(existingRoom);
                });
    }

    @Override
    public Mono<RoomEntity> uploadImage(Integer roomId, String filePath) {
        return roomRepository.findById(roomId)
                .flatMap(existingRoom -> {
                    existingRoom.setImage(filePath);
                    return roomRepository.save(existingRoom);
                });
    }

    @Override
    public Mono<Void> deleteRoom(Integer roomId) {
        return roomRepository.deleteById(roomId);
    }

    @Override
    public Flux<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<RoomEntity> getRoomById(Integer roomId) {
        return roomRepository.findById(roomId);
    }


    @Override
    public Flux<ViewRoomReturn> findAllView() {
        return roomRepository.findAllViewRoomReturn()
                .flatMap(room ->
                        roomRepository.findAllViewBedroomReturn(room.getRoomId())
                                .collectList()
                                .map(bed -> {
                                    room.setListBedroomReturn(bed);
                                    return room;
                                })
                );
    }
}
