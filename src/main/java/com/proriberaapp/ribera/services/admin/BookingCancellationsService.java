package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.bookingcancellations.request.BookingCancellationRequest;
import com.proriberaapp.ribera.Domain.entities.BookingCancellationsEntity;
import reactor.core.publisher.Mono;

public interface BookingCancellationsService {

    Mono<BookingCancellationsEntity> createBookingCancellation(BookingCancellationRequest request);
}
