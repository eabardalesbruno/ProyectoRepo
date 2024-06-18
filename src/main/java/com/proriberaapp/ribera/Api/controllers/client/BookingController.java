package com.proriberaapp.ribera.Api.controllers.client;
import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.client.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final JwtProvider jtp;
    private final BookingService bookingService;

    @GetMapping("/find/all/state")
    public Flux<ViewBookingReturn> findAllByStateBookings(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByUserClientIdAndBookingStateIdIn(userClientId, stateId);
    }

    @GetMapping("/find/all")
    public Flux<ViewBookingReturn> findAllBookings(
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByUserClientIdAndBookingIn(userClientId);
    }

    @GetMapping("/find")
    public Mono<BookingEntity> findBooking(
            @RequestParam("bookingId") Integer bookingId,
            @RequestHeader("Authorization") String token) {
        Integer idUserAdmin = jtp.getIdFromToken(token);
        return bookingService.findByIdAndIdUserAdmin(idUserAdmin, bookingId);
    }

    @GetMapping("/{bookingId}/costfinal")
    public Mono<ResponseEntity<BigDecimal>> getCostFinalByBookingId(@PathVariable Integer bookingId) {
        return bookingService.getCostFinalByBookingId(bookingId)
                .map(costFinal -> ResponseEntity.ok().body(costFinal))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{bookingId}")
    public Mono<ResponseEntity<BookingEntity>> findBookingById(@PathVariable Integer bookingId) {
        return bookingService.findByBookingId(bookingId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/calendarDate")
    public Flux<CalendarDate> downloadBoucher(
            @RequestParam("roomOfferId") Integer id) {
        return bookingService.calendarDate(id);
    }
}
