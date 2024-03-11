package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingDetailRepository extends R2dbcRepository<BookingDetailEntity, Integer> {
}
