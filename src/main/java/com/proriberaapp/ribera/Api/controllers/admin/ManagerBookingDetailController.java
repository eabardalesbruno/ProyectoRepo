package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BookingDetailEntity;
import com.proriberaapp.ribera.Infraestructure.services.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/booking-detail")
@RequiredArgsConstructor
public class ManagerBookingDetailController {
    private final BookingDetailService bookingDetailService;

    @GetMapping("/find/all")
    public Flux<BookingDetailEntity> getAllBookingDetails() {
        return bookingDetailService.findAll();
    }

    @GetMapping("/find")
    public Mono<BookingDetailEntity> getBookingDetailById(Integer id) {
        return bookingDetailService.findById(id);
    }

    @PostMapping("/register")
    public Mono<BookingDetailEntity> registerBookingDetail() {
        return bookingDetailService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<BookingDetailEntity> registerAllBookingDetails() {
        return bookingDetailService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<BookingDetailEntity> updateBookingDetail() {
        return bookingDetailService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteBookingDetail(Integer id) {
        return bookingDetailService.deleteById(id);
    }


}
