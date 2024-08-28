package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoomTypeRepository extends R2dbcRepository<RoomTypeEntity, Integer> {
    Mono<RoomTypeEntity> findByRoomTypeName(String roomTypeName);
    Flux<RoomTypeEntity> findAllByRoomTypeNameIn(List<String> roomTypeNames);}
