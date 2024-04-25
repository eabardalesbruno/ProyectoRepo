package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoomDetailRepository extends R2dbcRepository<RoomDetailEntity, Integer> {
}
