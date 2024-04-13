package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/find/all")
    public Flux<BookingEntity> findAllBookings() {
        return bookingService.findAll();
    }

    @GetMapping("/find")
    public Mono<BookingEntity> findBooking(Integer id) {
        return bookingService.findById(id);
    }
}
