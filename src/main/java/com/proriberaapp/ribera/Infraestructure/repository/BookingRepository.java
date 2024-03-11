package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {
    Mono<Object> findByUserIdAndPaymentMethodIdAndBookingStateId(Integer userId, Integer paymentMethodId, Integer bookingStateId);
    Flux<Object> findByUserIdAndPaymentMethodIdAndBookingStateId(Flux<Integer> userId, Flux<Integer> paymentMethodId, Flux<Integer> bookingStateId);
}
