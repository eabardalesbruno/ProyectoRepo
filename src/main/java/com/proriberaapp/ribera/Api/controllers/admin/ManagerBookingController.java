package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/booking")
@RequiredArgsConstructor
public class ManagerBookingController {
    private final BookingService bookingService;

    @GetMapping("/find/all")
    public Flux<BookingEntity> findAllBookings() {
        return bookingService.findAll();
    }

    @GetMapping("/find")
    public Mono<BookingEntity> findBooking(Integer id) {
        return bookingService.findById(id);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteBooking(Integer id) {
        return bookingService.deleteById(id);
    }

    @PostMapping("/register")
    public Mono<BookingEntity> registerBooking(@RequestBody BookingEntity bookingEntity) {
        return bookingService.save(bookingEntity);
    }

    @PostMapping("/register/all")
    public Flux<BookingEntity> registerAllBookings(@RequestBody List<BookingEntity> bookingEntity) {
        return bookingService.saveAll(bookingEntity);
    }

    @PatchMapping("/update")
    public Mono<BookingEntity> updateBooking(@RequestBody BookingEntity bookingEntity) {
        return bookingService.update(bookingEntity);
    }
}
