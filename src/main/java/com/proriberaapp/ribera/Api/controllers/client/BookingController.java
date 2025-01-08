package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingStates;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;
import com.proriberaapp.ribera.Domain.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.CompanionsService;
import com.proriberaapp.ribera.services.client.impl.CompanionServiceImpl;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final JwtProvider jtp;
    private final BookingService bookingService;
    private final CompanionServiceImpl companionsService;

    @GetMapping("/find/all/state")
    public Flux<ViewBookingReturn> findAllByStateBookings(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByUserClientIdAndBookingStateIdIn(userClientId, stateId);
    }

    @GetMapping("/find/booking-by-id")
    public Mono<ViewBookingReturn> findById(
            @RequestParam("stateId") Integer stateId,
            @RequestParam("bookingId") Integer bookingId,
            @RequestParam("userClientId") Integer userClientId,
            @RequestHeader("Authorization") String token) {
        return bookingService.findByUserClientIdAndBookingIdAndBookingStateIdIn(userClientId, bookingId, stateId);
    }

    @GetMapping("/find/all/state/by-promoter")
    public Flux<ViewBookingReturn> findAllByStateBookingsByPromoter(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userPromoterId = jtp.getIdFromToken(token);
        return bookingService.findAllByUserPromoterIdAndBookingStateIdIn(userPromoterId, stateId);
    }

    @GetMapping("/find/all/state/by-receptionist")
    public Flux<ViewBookingReturn> findAllByStateBookingsByReceptionist(
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer receptionistId = jtp.getIdFromToken(token);
        return bookingService.findAllByReceptionistIdAndBookingStateIdIn(receptionistId, stateId);
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
        return bookingService.findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(dayBookingInit,
                dayBookingEnd, userClientId, stateId);
    }

    @GetMapping("/find/all/family")
    public Flux<ViewBookingReturn> findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
            @RequestParam("numberAdults") Integer numberAdults,
            @RequestParam("numberChildren") Integer numberChildren,
            @RequestParam("numberBabies") Integer numberBabies,
            @RequestParam("stateId") Integer stateId,
            @RequestHeader("Authorization") String token) {
        Integer userClientId = jtp.getIdFromToken(token);
        return bookingService.findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(
                numberAdults, numberChildren, numberBabies, userClientId, stateId);
    }

    @GetMapping("/find/all/states")
    public Flux<ViewBookingReturn> findAllByStateBookings(@RequestParam("stateId") Integer stateId) {
        return bookingService.findAllByBookingStateId(stateId);
    }

    // RESERVAS JMANRIQUE
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
     * @PostMapping("/save")
     * public Mono<BookingEntity> saveBooking(
     * 
     * @RequestBody BookingSaveRequest bookingSaveRequest,
     * 
     * @RequestHeader("Authorization") String token) {
     * Integer userClientId = jtp.getIdFromToken(token);
     * log.info("userClientId: {}", userClientId);
     * log.info("bookingSaveRequest: {}", bookingSaveRequest);
     * return bookingService.save(userClientId, bookingSaveRequest);
     * }
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
            Boolean isReceptionist = Role.ROLE_RECEPTIONIST.name().equalsIgnoreCase(authority)
                    || Role.ROLE_SUPER_ADMIN.name().equalsIgnoreCase(authority);
            System.out.println("isPromoter: " + isPromoter + " isReceptionist: " + isReceptionist);
            return new Object[] { userClientId, isPromoter, isReceptionist };
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
                .flatMap(userClientId -> bookingService.save(0, bookingSaveRequest, false, false));
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
    public Mono<ResponseEntity<BookingEntity>> assignClientToBooking(@PathVariable Integer bookingId,
            @PathVariable Integer userId) {
        return bookingService.assignClientToBooking(bookingId, userId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{bookingId}/companionsDetails")
    public Flux<CompanionsEntity> getCompanionsByBookingId(@PathVariable Integer bookingId) {
        return companionsService.getCompanionsByBookingId(bookingId);
    }

    @PostMapping("/{bookingId}/companions")
    public Mono<Void> addCompanionsToBooking(@PathVariable Integer bookingId,
            @RequestBody List<Map<String, Object>> companionsData) {
        Flux<CompanionsEntity> companionsEntityFlux = Flux.fromIterable(companionsData)
                .map(data -> {
                    CompanionsEntity companion = new CompanionsEntity();
                    companion.setFirstname((String) data.get("nombres"));
                    companion.setLastname((String) data.get("apellidos"));
                    companion.setTypeDocumentId(
                            data.get("typeDocument") != null ? ((Number) data.get("typeDocument")).intValue() : null); // Tipo
                                                                                                                       // de
                                                                                                                       // documento
                    companion.setDocumentNumber((String) data.get("document"));
                    companion.setCellphone((String) data.get("celphone"));
                    companion.setEmail((String) data.get("correo"));
                    companion.setTitular(Boolean.TRUE.equals(data.get("isTitular")));
                    companion.setCategory((String) data.get("category"));

                    String birthdateStr = (String) data.get("fechaNacimiento");
                    if (birthdateStr != null) {
                        try {
                            LocalDateTime birthdate = ZonedDateTime.parse(birthdateStr).toLocalDateTime();
                            companion.setBirthdate(Timestamp.valueOf(birthdate));
                        } catch (DateTimeParseException e) {
                            throw new RuntimeException("Formato de fecha inválido: " + birthdateStr, e);
                        }
                    }
                    companion.setGenderId(
                            data.get("genero") != null ? ("Masculino".equals(data.get("genero")) ? 1 : 2) : null);
                    companion.setCountryId(
                            data.get("areaZone") != null ? ((Number) data.get("areaZone")).intValue() : null);

                    return companion;
                });

        return companionsEntityFlux.collectList()
                .flatMap(companions -> {
                    boolean hasTitular = companions.stream().anyMatch(CompanionsEntity::isTitular);
                    if (!hasTitular) {
                        return Mono.error(new IllegalArgumentException("Debe haber al menos un titular en la reserva"));
                    }

                    if (companions.size() > 1) {
                        return companionsService.validateTotalCompanions(bookingId, Flux.fromIterable(companions))
                                .thenMany(Flux.fromIterable(companions)
                                        .flatMap(companion -> {
                                            companion.setBookingId(bookingId);
                                            return companionsService.calculateAgeandSave(companion);
                                        }))
                                .then();
                    }

                    CompanionsEntity titular = companions.stream()
                            .filter(CompanionsEntity::isTitular)
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Titular no encontrado"));
                    titular.setBookingId(bookingId);
                    return companionsService.calculateAgeandSave(titular).then();
                });
    }

    @PutMapping("/{bookingId}/companionupdate")
    public Mono<List<CompanionsEntity>> updateCompanions(
            @PathVariable Integer bookingId,
            @RequestBody List<Map<String, Object>> companionsData) {

        List<CompanionsEntity> companionsEntities = companionsData.stream()
                .map(data -> {
                    CompanionsEntity companion = new CompanionsEntity();
                    companion.setCompanionId(data.get("companionId") != null ? ((Number) data.get("companionId")).intValue() : null);
                    companion.setFirstname((String) data.get("nombres"));
                    companion.setLastname((String) data.get("apellidos"));
                    companion.setTypeDocumentId(data.get("typeDocument") != null ? ((Number) data.get("typeDocument")).intValue() : null);
                    companion.setDocumentNumber((String) data.get("document"));
                    companion.setCellphone((String) data.get("celphone"));
                    companion.setEmail((String) data.get("correo"));
                    companion.setCategory((String) data.get("category"));

                    String birthdateStr = (String) data.get("fechaNacimiento");
                    if (birthdateStr != null) {
                        try {
                            LocalDateTime birthdate = ZonedDateTime.parse(birthdateStr).toLocalDateTime();
                            companion.setBirthdate(Timestamp.valueOf(birthdate));
                        } catch (DateTimeParseException e) {
                            throw new RuntimeException("Formato de fecha inválido: " + birthdateStr, e);
                        }
                    }

                    companion.setGenderId(data.get("genero") != null ? ("Masculino".equals(data.get("genero")) ? 1 : 2) : null);
                    companion.setCountryId(data.get("areaZone") != null ? ((Number) data.get("areaZone")).intValue() : null);

                    return companion;
                })
                .collect(Collectors.toList());

        return companionsService.updateCompanion(bookingId, companionsEntities)
                .collectList();
    }


    @GetMapping("/companions/dni/{dni}")
    public ResponseEntity<CompanionsDto> getCompanionByDni(@PathVariable String dni) {
        try {
            CompanionsDto companion = companionsService.fetchCompanionByDni(dni).block();
            return ResponseEntity.ok(companion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
