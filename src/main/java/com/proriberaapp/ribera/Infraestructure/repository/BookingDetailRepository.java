package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookingDetailRepository extends R2dbcRepository<BookingDetailEntity, Integer> {
<<<<<<< HEAD
    Mono<BookingDetailEntity> findByBookingId(BookingDetailEntity bookingDetailEntity);
    Flux<BookingDetailEntity> findAllByBookingIdIn(List<BookingDetailEntity> bookingDetailEntity);
=======
    Mono<BookingDetailEntity> findByRoomIdAndBookingIdAndPaymentStateId(Integer roomId, Integer bookingId, Integer paymentStateId);
    Flux<BookingDetailEntity> findByRoomIdAndBookingIdAndPaymentStateId(Flux<Integer> roomId, Flux<Integer> bookingId, Flux<Integer> paymentStateId);
    Flux<BookingDetailEntity> findByRoomType(String roomType);

>>>>>>> jose-dev
}
