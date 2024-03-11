package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoomRepository extends R2dbcRepository<RoomEntity, Integer>{
}
