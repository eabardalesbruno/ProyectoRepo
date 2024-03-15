package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Infraestructure.services.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/manager/booking-detail")
@RequiredArgsConstructor
public class ManagerBookingDetailController {
    private final BookingDetailService bookingDetailService;
}
