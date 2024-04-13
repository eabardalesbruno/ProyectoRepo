package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StateRoomRepository extends R2dbcRepository<StateRoomEntity, Integer> {
    Mono<StateRoomEntity> findByStateRoomName(StateRoomEntity stateRoomName);
    Flux<StateRoomEntity> findAllByStateRoomNameIn(List<StateRoomEntity> stateRoomName);
}
