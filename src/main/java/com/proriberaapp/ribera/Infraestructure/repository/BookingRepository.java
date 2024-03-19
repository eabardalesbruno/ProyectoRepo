package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {
    Mono<BookingEntity> findByBookingStateId(BookingEntity bookingEntity);
    Flux<BookingEntity> findAllByBookingStateIdIn(List<BookingEntity> bookingEntity);
}
