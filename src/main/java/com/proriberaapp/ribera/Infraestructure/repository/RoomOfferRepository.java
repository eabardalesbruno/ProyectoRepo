package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RoomOfferRepository extends R2dbcRepository<RoomOfferEntity, Integer> {
}
