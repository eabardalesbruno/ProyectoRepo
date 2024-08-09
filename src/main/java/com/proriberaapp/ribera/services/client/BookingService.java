package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BaseService;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BookingService extends BaseService<BookingEntity,BookingEntity> {
    Mono<S3UploadResponse> loadBoucher(Mono<FilePart> file, Integer folderNumber, String token);
    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId, Integer stateId);
    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingIn(Integer userClientId);
    Flux<ViewBookingReturn> findAllView();

    Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId);
    Mono<BigDecimal> getCostFinalByBookingId(Integer bookingId);

    Mono<BookingEntity> findByBookingId(Integer bookingId);

    Mono<BookingEntity> updateBookingStatePay(Integer bookingId, Integer bookingStateId);

    Flux<CalendarDate> calendarDate(Integer id);

    Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest bookingSaveRequest);
    Mono<BigDecimal> getRiberaPointsByBookingId(Integer bookingId);
    Mono<Integer> getUserClientIdByBookingId(Integer bookingId);

}
