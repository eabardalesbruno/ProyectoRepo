package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingCancellationsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingCancellationsRepository extends R2dbcRepository<BookingCancellationsEntity,Integer> {
}
