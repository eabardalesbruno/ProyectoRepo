package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingDetailService {
    Mono<BookingDetailEntity> save(BookingDetailEntity bookingDetailEntity);
    Flux<BookingDetailEntity> saveAll(Flux<BookingDetailEntity> bookingDetailEntity);
    Mono<BookingDetailEntity> findById(String id);
    Flux<BookingDetailEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<BookingDetailEntity> update(BookingDetailEntity bookingDetailEntity);
    Flux<BookingDetailEntity> findByRoomType(String roomType);

}
