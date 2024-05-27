package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBooking;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBookingAvailability;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBookingInventory;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Api.controllers.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface BookingService extends BaseService<BookingEntity,BookingEntity> {
    Mono<S3UploadResponse> loadBoucher(Mono<FilePart> file, Integer folderNumber, String token);
    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId, Integer stateId);
    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingIn(Integer userClientId);
    Flux<ViewBookingReturn> findAllView();

    Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId);
}
