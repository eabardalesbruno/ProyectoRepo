package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingStateService {
    Mono<BookingStateEntity> save(BookingStateEntity bookingStateEntity);
    Flux<BookingStateEntity> saveAll(Flux<BookingStateEntity> bookingStateEntity);
    Mono<BookingStateEntity> findById(String id);
    Flux<BookingStateEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<BookingStateEntity> update(BookingStateEntity bookingStateEntity);
}
