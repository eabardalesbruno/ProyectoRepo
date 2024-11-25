package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.client.BookingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reports")
public class ReportManagerController {
    private final BookingService bookingService;

    public ReportManagerController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bookings/by-state")
    public Flux<BookingEntity> getBookingsByState(@RequestParam Integer stateId) {
        return bookingService.findBookingsByStateId(stateId);
    }
}
