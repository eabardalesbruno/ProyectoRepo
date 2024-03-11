package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {
}
