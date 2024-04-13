package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookingStateRepository extends R2dbcRepository<BookingStateEntity, Integer> {
    Mono<Object> findByBookingStateName(String bookingStateName);
    Flux<Object> findAllByBookingStateNameIn(List<BookingStateEntity> bookingStateName);
}
