package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface BookingService extends BaseService<BookingEntity,BookingEntity> {
    Mono<S3UploadResponse> loadBoucher(Resource file, String token) throws IOException;
    Flux<BookingEntity> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId, Integer bookingStateId);

    Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId);
}
