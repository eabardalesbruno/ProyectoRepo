package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    @Autowired
    private JwtTokenProvider jtp;
    private final BookingService bookingService;

    @GetMapping("/find/all/state")
    public Flux<BookingEntity> findAllBookings(
            @RequestParam("userClientId") Integer userClientId,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        return bookingService.findAllByUserClientIdAndBookingStateIdIn(idUserAdmin, userClientId);
    }

    @GetMapping("/find")
    public Mono<BookingEntity> findBooking(
            @RequestParam("bookingId") Integer bookingId,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        return bookingService.findByIdAndIdUserAdmin(idUserAdmin, bookingId);
    }
}
