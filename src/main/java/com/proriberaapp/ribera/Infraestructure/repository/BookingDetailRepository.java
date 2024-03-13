package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingDetailRepository extends R2dbcRepository<BookingDetailEntity, Integer> {
    Mono<BookingDetailEntity> findByBookingIdAndPaymentStateId(Integer bookingId, Integer paymentStateId);
    Flux<BookingDetailEntity> findByBookingIdAndPaymentStateId(Flux<Integer> bookingId, Flux<Integer> paymentStateId);
}
