package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BedroomRepository extends R2dbcRepository<BedroomEntity, Integer> {
    Mono<BedroomEntity> findByRoomId(Integer roomId);

    Flux<BedroomEntity> findAllByRoomIdIn(List<BedroomEntity> entity);

    Flux<BedroomEntity> findAllByBedTypeId(Integer bedTypeId);

    @Query("SELECT * FROM ViewBedroomReturn WHERE roomid = :roomid")
    Flux<BedroomReturn> findAllViewBedroomReturn(@Param("roomid") Integer roomid);
}
