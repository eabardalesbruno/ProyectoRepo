package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingcancellations.request.BookingCancellationRequest;
import com.proriberaapp.ribera.Domain.entities.BookingCancellationsEntity;
import com.proriberaapp.ribera.services.admin.BookingCancellationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.manager}/booking-cancellations")
@RequiredArgsConstructor
public class ManagerBookingCancellationsController {

    private final BookingCancellationsService bookingCancellationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookingCancellationsEntity> createBookingCancellations(@RequestBody BookingCancellationRequest request) {
        return bookingCancellationsService.createBookingCancellation(request);
    }
}
