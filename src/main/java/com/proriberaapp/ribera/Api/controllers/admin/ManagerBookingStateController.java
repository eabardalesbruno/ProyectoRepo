package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import com.proriberaapp.ribera.services.BookingStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/booking-state")
@RequiredArgsConstructor
public class ManagerBookingStateController {
    private final BookingStateService bookingStateService;

    @PostMapping("/register")
    public Mono<BookingStateEntity> registerBookingState(
            @RequestBody BookingStateEntity bookingStateEntity
    ) {
        return bookingStateService.save(bookingStateEntity);
    }

    @PostMapping("/register/all")
    public Flux<BookingStateEntity> registerAllBookingStates(
            @RequestBody List<BookingStateEntity> bookingStateEntity
    ) {
        return bookingStateService.saveAll(bookingStateEntity);
    }

    @PatchMapping("/update")
    public Mono<BookingStateEntity> updateBookingState(
            @RequestBody BookingStateEntity bookingStateEntity
    ) {
        return bookingStateService.update(bookingStateEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteBookingState(
            @RequestParam Integer id
    ) {
        return bookingStateService.deleteById(id);
    }

    @GetMapping("/find")
    public Mono<BookingStateEntity> findBookingState(
            @RequestParam Integer id
    ) {
        return bookingStateService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<BookingStateEntity> findAllBookingStates() {
        return bookingStateService.findAll();
    }

}
