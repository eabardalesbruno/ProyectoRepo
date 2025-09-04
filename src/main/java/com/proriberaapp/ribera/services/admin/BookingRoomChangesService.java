package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.request.BookingRoomChangesRequest;
import com.proriberaapp.ribera.Domain.entities.BookingRoomChangesEntity;
import reactor.core.publisher.Mono;

public interface BookingRoomChangesService {

    Mono<BookingRoomChangesEntity> createBookingRoomChangeAndUpdateBooking(BookingRoomChangesRequest request);
}
