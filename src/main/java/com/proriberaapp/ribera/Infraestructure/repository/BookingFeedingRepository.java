package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.BookingFeedingDto;
import com.proriberaapp.ribera.Domain.entities.BookingFeedingEntity;

import reactor.core.publisher.Flux;

import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BookingFeedingRepository extends R2dbcRepository<BookingFeedingEntity, Integer> {

    @Query("""
            select * from viewbookingfeeding
            where bookingId in (:bookings)
            """)
    Flux<BookingFeedingDto> listBookingFeedingByBookingId(List<Integer> bookings);
}
