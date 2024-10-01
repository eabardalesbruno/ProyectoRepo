package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomReturn;
import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomRepository extends R2dbcRepository<RoomEntity, Integer>{
    Mono<RoomEntity> findByRoomName(String roomName);
    Flux<RoomEntity> findAllByRoomNameIn(List<String> roomName);
    Flux<RoomEntity> findAllByRoomTypeId(Integer roomTypeId);

    @Query("SELECT * FROM ViewRoomReturn")
    Flux<ViewRoomReturn> findAllViewRoomReturn();

    @Query("SELECT * FROM ViewBedroomReturn WHERE roomid = :roomid")
    Flux<BedroomReturn> findAllViewBedroomReturn(@Param("roomid") Integer roomid);
}
