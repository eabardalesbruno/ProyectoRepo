package com.proriberaapp.ribera.Api.controllers;
import com.proriberaapp.ribera.Api.controllers.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final JwtProvider jtp;
    private final BookingService bookingService;

    @GetMapping("/find/all/state")
    public Flux<ViewBookingReturn> findAllBookings(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        log.info("Finding all bookings with stateId: " + stateId + " and userClientId: " + userClientId);
        return bookingService.findAllByUserClientIdAndBookingStateIdIn(userClientId, stateId);
    }

    @GetMapping("/find")
    public Mono<BookingEntity> findBooking(
            @RequestParam("bookingId") Integer bookingId,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        return bookingService.findByIdAndIdUserAdmin(idUserAdmin, bookingId);
    }
}
