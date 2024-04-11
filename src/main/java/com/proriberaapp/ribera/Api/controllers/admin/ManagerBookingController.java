package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Crosscutting.security.JwtTokenProvider;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BaseService;
import com.proriberaapp.ribera.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

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
