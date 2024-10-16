package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingFeedingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingFeedingRepository extends R2dbcRepository<BookingFeedingEntity, Integer> {
}
