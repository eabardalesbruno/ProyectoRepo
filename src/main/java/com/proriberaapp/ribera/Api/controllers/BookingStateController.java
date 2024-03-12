package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import com.proriberaapp.ribera.Infraestructure.services.BookingStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/booking-state")
@RequiredArgsConstructor
public class BookingStateController {
    private final BookingStateService bookingStateService;

    @GetMapping("/find/all")
    public Flux<BookingStateEntity> findAllBookingStates() {
        return bookingStateService.findAll();
    }
}
