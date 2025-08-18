package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.request.BookingRoomChangesRequest;
import com.proriberaapp.ribera.Domain.entities.BookingRoomChangesEntity;
import com.proriberaapp.ribera.services.admin.BookingRoomChangesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.manager}/booking-room-changes")
@RequiredArgsConstructor
public class ManagerBookingRoomChangesController {

    private final BookingRoomChangesService bookingRoomChangesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookingRoomChangesEntity> createBookingRoomChangesAndUpdateBooking(
            @RequestBody BookingRoomChangesRequest request) {
        return bookingRoomChangesService.createBookingRoomChangeAndUpdateBooking(request);
    }
}
