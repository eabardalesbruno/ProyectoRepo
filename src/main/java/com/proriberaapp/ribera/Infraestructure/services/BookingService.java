package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {
    Mono<BookingEntity> save(BookingEntity bookingEntity);
    Flux<BookingEntity> saveAll(Flux<BookingEntity> bookingEntity);
    Mono<BookingEntity> findById(String id);
    Flux<BookingEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<BookingEntity> update(BookingEntity bookingEntity);
}
