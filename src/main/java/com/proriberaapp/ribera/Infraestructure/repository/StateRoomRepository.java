package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface StateRoomRepository extends R2dbcRepository<StateRoomEntity, Integer> {
}
