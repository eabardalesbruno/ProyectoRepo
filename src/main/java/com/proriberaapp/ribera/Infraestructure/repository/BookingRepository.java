package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends R2dbcRepository<BookingEntity, Integer> {
    Mono<BookingEntity> findByBookingStateId(BookingEntity bookingEntity);
    Flux<BookingEntity> findAllByBookingStateIdIn(List<BookingEntity> bookingEntity);
    @Query("SELECT * FROM booking WHERE roomofferid = :roomOfferId AND ((daybookinginit <= :dayBookingEnd) AND (daybookingend >= :dayBookingInit))")
    Flux<BookingEntity> findExistingBookings(@Param("roomOfferId") Integer roomOfferId, @Param("dayBookingInit") Timestamp dayBookingInit, @Param("dayBookingEnd") Timestamp dayBookingEnd);
}
