package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ComfortRoomOfferDetailRepository extends R2dbcRepository<ComfortRoomOfferDetailEntity, Integer> {
}
