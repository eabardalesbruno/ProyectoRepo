package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;

public interface RoomTypeService extends BaseService<RoomTypeEntity, RoomTypeEntity> {
    Flux<RoomTypeEntity> getAllRoomTypes();

}
