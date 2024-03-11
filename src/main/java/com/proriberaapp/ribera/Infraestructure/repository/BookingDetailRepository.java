package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingDetailRepository extends R2dbcRepository<BookingDetailEntity, Integer> {
    Mono<BookingDetailEntity> findByRoomIdAndBookingIdAndPaymentStateId(Integer roomId, Integer bookingId, Integer paymentStateId);
    Flux<BookingDetailEntity> findByRoomIdAndBookingIdAndPaymentStateId(Flux<Integer> roomId, Flux<Integer> bookingId, Flux<Integer> paymentStateId);
}
