package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BedroomRepository extends R2dbcRepository<BedroomEntity, Integer>{
    Mono<BedroomEntity> findByRoomId(Integer roomId);

    Flux<BedroomEntity> findAllByRoomIdIn(List<BedroomEntity> entity);
}
