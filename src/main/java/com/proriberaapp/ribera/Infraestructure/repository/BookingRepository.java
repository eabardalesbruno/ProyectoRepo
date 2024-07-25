package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
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

    Flux<BookingEntity> findAllByUserClientIdAndBookingStateId(Integer userClientId, Integer bookingStateId);

    @Query("SELECT * FROM booking WHERE roomofferid = :roomOfferId AND ((daybookinginit <= :dayBookingEnd) AND (daybookingend >= :dayBookingInit))")
    Flux<BookingEntity> findExistingBookings(@Param("roomOfferId") Integer roomOfferId, @Param("dayBookingInit") Timestamp dayBookingInit, @Param("dayBookingEnd") Timestamp dayBookingEnd);

    Mono<BookingEntity> findByBookingIdAndUserClientId(Integer userClientId, Integer bookingId);

    @Query("SELECT * FROM ViewBookingReturn WHERE userClientId = :userClientId AND bookingStateId = :bookingStateId")
    Flux<ViewBookingReturn> findAllViewBookingReturnByUserClientIdAndBookingStateId(@Param("userClientId") Integer userClientId, @Param("bookingStateId") Integer bookingStateId);

    @Query("SELECT * FROM ViewBookingReturn WHERE userClientId = :userClientId")
    Flux<ViewBookingReturn> findAllViewBookingReturnByUserClientId(@Param("userClientId") Integer userClientId);

    @Query("SELECT * FROM ViewBookingReturn WHERE bookingId = :bookingId")
    Mono<ViewBookingReturn> findAllViewBookingReturnByBookingId(@Param("bookingId") Integer bookingId);


    @Query("SELECT * FROM ViewBookingReturn")
    Flux<ViewBookingReturn> findAllViewBookingReturn();

    @Query("SELECT bookingid, roomofferid, daybookinginit, daybookingend FROM booking WHERE roomofferid = :roomOfferId AND dayBookingEnd >= CURRENT_DATE")
    Flux<CalendarDate> findAllCalendarDate(@Param("roomofferid") Integer roomOfferId);
    Mono<BookingEntity> findByBookingId(Integer bookingId);

}
