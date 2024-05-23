package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import reactor.core.publisher.Flux;

public interface RoomService extends BaseService<RoomEntity, RoomEntity> {
    Flux<ViewRoomReturn> findAllView();
}
