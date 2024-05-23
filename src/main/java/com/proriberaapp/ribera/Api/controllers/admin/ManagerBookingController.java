package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.*;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Api.controllers.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.services.BookingService;
import com.proriberaapp.ribera.services.admin.BookingManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("${url.manager}/booking")
@RequiredArgsConstructor
@Slf4j
public class ManagerBookingController extends BaseManagerController<BookingEntity, BookingEntity>{

    private final BookingService bookingService;
    private final BookingManagerService bookingManagerService;

    @PostMapping("load/boucher")
    public Mono<S3UploadResponse> loadBoucher(
            @RequestBody Resource file,
            @RequestHeader("Authorization") String token) throws IOException {
        return bookingService.loadBoucher(file, token);
    }

    /* FIND ALL */
    @GetMapping("/find/all/viewBooking")
    public Flux<ViewAdminBookingReturn> viewAdminBookingReturn(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) Integer roomOfferId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer bookingId,
            @RequestParam(required = false) Integer bookingStateId,
            @RequestParam(required = false) Integer userClientId,
            @RequestParam(required = false) String dateCreate,
            @RequestParam(required = false) String numberBooking,
            @RequestParam(required = false) String typeRoom,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String numberDocument,
            @RequestParam(required = false) String costTotal) {
        //Integer userClientId = jtp.getIdFromToken(token);
        SearchFiltersBooking filters = new SearchFiltersBooking(
                roomId, roomOfferId, roomTypeId, bookingId, bookingStateId, userClientId, dateCreate, numberBooking,
                typeRoom, client, numberDocument, costTotal
        );
        return bookingManagerService.viewAdminBookingReturn(filters);
    }

    @GetMapping("/find/all/viewInventory")
    public Flux<ViewAdminBookingInventoryReturn> viewAdminBookingInventoryReturn(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) Integer roomOfferId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer bookingId,
            @RequestParam(required = false) Integer bookingStateId,
            @RequestParam(required = false) Integer userClientId,
            @RequestParam(required = false) String dateCreate,
            @RequestParam(required = false) String numberBooking,
            @RequestParam(required = false) String typeRoom,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String numberDocument,
            @RequestParam(required = false) String costTotal) {
        //Integer userClientId = jtp.getIdFromToken(token);
        SearchFiltersBookingInventory filters = new SearchFiltersBookingInventory(
                roomId, roomOfferId, roomTypeId, bookingId, bookingStateId, userClientId, dateCreate, numberBooking,
                typeRoom, client, numberDocument, costTotal
        );
        return bookingManagerService.viewAdminBookingInventoryReturn(filters);
    }

    @GetMapping("/find/all/viewAvailability")
    public Flux<ViewAdminBookingAvailabilityReturn> viewAdminBookingAvailabilityReturn(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) Integer roomOfferId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) Integer bookingId,
            @RequestParam(required = false) Integer bookingStateId,
            @RequestParam(required = false) Integer userClientId,
            @RequestParam(required = false) String dateCreate,
            @RequestParam(required = false) String numberBooking,
            @RequestParam(required = false) String typeRoom,
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String numberDocument,
            @RequestParam(required = false) String costTotal) {
        //Integer userClientId = jtp.getIdFromToken(token);
        SearchFiltersBookingAvailability filters = new SearchFiltersBookingAvailability(
                roomId, roomOfferId, roomTypeId, bookingId, bookingStateId, userClientId, dateCreate, numberBooking,
                typeRoom, client, numberDocument, costTotal
        );
        return bookingManagerService.viewAdminBookingAvailabilityReturn(filters);
    }

    /* FILTERS */
    @GetMapping("/find/filters")
    public Mono<FindFiltersBooking> findFiltersBooking(
            @RequestHeader("Authorization") String token) {
        //Integer userClientId = jtp.getIdFromToken(token);
        return bookingManagerService.findFiltersBooking();
    }

}
