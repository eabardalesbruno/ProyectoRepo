package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;

public interface RoomService extends BaseService<RoomEntity, RoomEntity> {
    Flux<ViewRoomReturn> findAllView();
}
