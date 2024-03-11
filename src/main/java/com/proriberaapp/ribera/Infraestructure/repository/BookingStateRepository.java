package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingStateRepository extends R2dbcRepository<BookingStateEntity, Integer> {
}
