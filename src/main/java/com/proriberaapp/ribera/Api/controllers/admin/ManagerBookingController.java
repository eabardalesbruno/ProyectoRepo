package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.client.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/manager/booking")
@Slf4j
public class ManagerBookingController extends BaseManagerController<BookingEntity, BookingEntity> {

    @Autowired
    private BookingService bookingService;

    @PostMapping(
            value = "load/boucher",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<S3UploadResponse> loadBoucher(
            @RequestPart("image") Mono<FilePart> image,
            @RequestParam("folderNumber") Integer folderNumber,
            @RequestHeader("Authorization") String token) {
        return bookingService.loadBoucher(image, folderNumber, token);
    }

    @GetMapping("/calendarDate")
    public Flux<CalendarDate> downloadBoucher(
            @RequestParam("roomOfferId") Integer id) {
        return bookingService.calendarDate(id);
    }
}
