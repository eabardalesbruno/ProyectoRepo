package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomService extends BaseService<RoomEntity, RoomEntity> {
    Flux<ViewRoomReturn> findAllView();
    Mono<RoomEntity> createRoom(RoomEntity room);

    Mono<RoomEntity> updateRoom(Integer roomId, RoomEntity room);

    Mono<Void> deleteRoom(Integer roomId);

    Flux<RoomEntity> getAllRooms();

    Mono<RoomEntity> getRoomById(Integer roomId);
}
