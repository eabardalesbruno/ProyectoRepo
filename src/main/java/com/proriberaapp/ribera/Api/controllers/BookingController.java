package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Infraestructure.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

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
