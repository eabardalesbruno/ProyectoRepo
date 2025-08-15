package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingRoomChangesEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingRoomChangesRepository extends R2dbcRepository<BookingRoomChangesEntity,Integer> {
}
