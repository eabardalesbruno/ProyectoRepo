package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/manager/booking")
@RequiredArgsConstructor
public class ManagerBookingController extends BaseManagerController<BookingEntity, BookingEntity>{
    private final BookingService bookingService;

    @PostMapping("load/boucher")
    public Mono<S3UploadResponse> loadBoucher(
            @RequestBody Resource file,
            @RequestHeader("Authorization") String token) throws IOException {
        return bookingService.loadBoucher(file, token);
    }
}
