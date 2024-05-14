package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.BookingStateEntity;
import com.proriberaapp.ribera.services.BookingStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/manager/booking-state")
@RequiredArgsConstructor
public class ManagerBookingStateController extends BaseManagerController<BookingStateEntity, BookingStateEntity>{
    private final BookingStateService bookingStateService;

}
