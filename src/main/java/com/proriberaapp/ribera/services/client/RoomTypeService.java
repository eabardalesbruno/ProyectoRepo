package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomTypeService extends BaseService<RoomTypeEntity, RoomTypeEntity> {
    Flux<RoomTypeEntity> getAllRoomTypes();
    Mono<RoomTypeEntity> createRoomType(RoomTypeEntity roomTypeEntity);

    Mono<RoomTypeEntity> updateRoomType(Integer id, RoomTypeEntity roomTypeEntity);

    Mono<Void> deleteRoomType(Integer id);
}
