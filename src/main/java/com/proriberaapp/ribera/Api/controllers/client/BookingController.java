package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingStates;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.services.client.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    @GetMapping("/find/all/state/by-promoter")
    public Flux<ViewBookingReturn> findAllByStateBookingsByPromoter(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userPromoterId = jtp.getIdFromToken(token);
        return bookingService.findAllByUserPromoterIdAndBookingStateIdIn(userPromoterId, stateId);
    }

    @GetMapping("/find/all/roomType")
    public Flux<ViewBookingReturn> findAllByRoomTypeIdAndUserClientIdAndBookingStateId(
            @RequestParam("roomTypeId") Integer roomTypeId,
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByRoomTypeIdAndUserClientIdAndBookingStateId(roomTypeId, userClientId, stateId);
    }

    @GetMapping("/find/all/dateRange")
    public Flux<ViewBookingReturn> findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(
            @RequestParam("dayBookingInit") Timestamp dayBookingInit,
            @RequestParam("dayBookingEnd") Timestamp dayBookingEnd,
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(dayBookingInit, dayBookingEnd, userClientId, stateId);
    }

    @GetMapping("/find/all/family")
    public Flux<ViewBookingReturn> findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
            @RequestParam("numberAdults") Integer numberAdults,
            @RequestParam("numberChildren") Integer numberChildren,
            @RequestParam("numberBabies") Integer numberBabies,
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(numberAdults, numberChildren, numberBabies, userClientId, stateId);
    }

    @GetMapping("/find/all/states")
    public Flux<ViewBookingReturn> findAllByStateBookings(@RequestParam("stateId") Integer stateId) {
        return bookingService.findAllByBookingStateId(stateId);
    }

    //RESERVAS JMANRIQUE
    @GetMapping("/all")
    public Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginated(
            @RequestParam Integer bookingStateId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offertimeInit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offertimeEnd,
            @RequestParam int page,
            @RequestParam int size) {
        return bookingService.findBookingsByStateIdPaginated(
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd, page, size);
    }

    @GetMapping("/all/user/{userId}")
    public Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginatedAndUserClientId(
            @RequestParam Integer bookingStateId, @PathVariable Integer userId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offertimeInit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime offertimeEnd,
            @RequestParam int page,
            @RequestParam int size) {
        return bookingService.findBookingsByStateIdPaginatedAndUserId(
                bookingStateId, roomTypeId, capacity, offertimeInit, offertimeEnd, page, size, userId);
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

    /*
    @PostMapping("/save")
    public Mono<BookingEntity> saveBooking(
            @RequestBody BookingSaveRequest bookingSaveRequest,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        log.info("userClientId: {}", userClientId);
        log.info("bookingSaveRequest: {}", bookingSaveRequest);
        return bookingService.save(userClientId, bookingSaveRequest);
    }
     */
    @PostMapping("/save")
    public Mono<BookingEntity> saveBooking(
            @RequestBody BookingSaveRequest bookingSaveRequest,
            @RequestHeader("Authorization") String token) {

        return Mono.fromCallable(() -> {
                    Integer userClientId = jtp.getIdFromToken(token);
                    String document = jtp.getDocumentFromToken(token);
                    Boolean isPromoter = (document != null && !document.isEmpty());
                    String authority = jtp.getAuthorityFromToken(token);
                    Boolean isReceptionist = Role.ROLE_RECEPTIONIST.name().equalsIgnoreCase(authority);
                    return new Object[]{userClientId, isPromoter,isReceptionist};
                })
                .flatMap(userInfo -> {
                    Integer userClientId = (Integer) userInfo[0];
                    Boolean isPromoter = (Boolean) userInfo[1];
                    Boolean isReceptionist = (Boolean) userInfo[2];
                    return bookingService.save(userClientId, bookingSaveRequest, isPromoter, isReceptionist);
                });
    }


    @PostMapping("/saveyes")
    public Mono<BookingEntity> saveBooking(@RequestBody BookingSaveRequest bookingSaveRequest) {
        return Mono.fromCallable(() -> {
                    Integer roomOfferId = bookingSaveRequest.getRoomOfferId();
                    log.info("roomOfferId: {}", roomOfferId);
                    log.info("bookingSaveRequest: {}", bookingSaveRequest);
                    return roomOfferId;
                })
                .flatMap(userClientId -> bookingService.save(0, bookingSaveRequest, false,false));
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

    @DeleteMapping("/all-old")
    public Mono<Boolean> deleteOldBookings() {
        return bookingService.deleteBookingNotPay();
    }

    @DeleteMapping("/{bookingId}")
    public Mono<Boolean> deleteBooking(@PathVariable Integer bookingId) {
        return bookingService.deleteBooking(bookingId);
    }

    @GetMapping("/assign-client/{userId}/bookingId/{bookingId}")
    public Mono<ResponseEntity<BookingEntity>> assignClientToBooking(@PathVariable Integer bookingId, @PathVariable Integer userId) {
        return bookingService.assignClientToBooking(bookingId, userId)
                .map(ResponseEntity::ok);
    }

}
