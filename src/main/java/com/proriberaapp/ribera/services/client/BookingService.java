package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.CalendarDate;
import com.proriberaapp.ribera.Api.controllers.admin.dto.S3UploadResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingSaveRequest;
import com.proriberaapp.ribera.Api.controllers.client.dto.BookingStates;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.dto.BookingFeedingDto;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.BaseService;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface BookingService extends BaseService<BookingEntity, BookingEntity> {
    Mono<S3UploadResponse> loadBoucher(Mono<FilePart> file, Integer folderNumber, String token);

    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingStateIdIn(Integer userClientId, Integer stateId);

    Flux<ViewBookingReturn> findAllByUserClientIdAndBookingIn(Integer userClientId);

    Flux<ViewBookingReturn> findAllView();

    Flux<ViewBookingReturn> findAllByBookingStateId(Integer bookingStateId);

    Mono<BookingEntity> findByIdAndIdUserAdmin(Integer idUserAdmin, Integer bookingId);

    Mono<BigDecimal> getCostFinalByBookingId(Integer bookingId);

    Mono<BookingEntity> findByBookingId(Integer bookingId);

    Mono<BookingEntity> updateBookingStatePay(Integer bookingId, Integer bookingStateId);

    Flux<CalendarDate> calendarDate(Integer id);

    Mono<BookingEntity> save(Integer userClientId, BookingSaveRequest bookingSaveRequest);

    Mono<BigDecimal> getRiberaPointsByBookingId(Integer bookingId);

    Mono<Integer> getUserClientIdByBookingId(Integer bookingId);

    Flux<ViewBookingReturn> findAllByRoomTypeIdAndUserClientIdAndBookingStateId(Integer roomTypeId, Integer userClientId, Integer bookingStateId);

    Flux<ViewBookingReturn> findAllByDayBookingInitAndDayBookingEndAndUserClientIdAndBookingStateId(Timestamp dayBookingInit, Timestamp dayBookingEnd, Integer userClientId, Integer bookingStateId);

    Flux<ViewBookingReturn> findAllByNumberAdultsAndNumberChildrenAndNumberBabiesAndUserClientIdAndBookingStateId(Integer numberAdults, Integer numberChildren, Integer numberBabies, Integer userClientId, Integer bookingStateId);

    Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginated(
            Integer bookingStateId,
            Integer roomTypeId,
            Integer capacity,
            LocalDateTime offertimeInit,
            LocalDateTime offertimeEnd,
            int page,
            int size);

    Mono<PaginatedResponse<BookingStates>> findBookingsByStateIdPaginatedAndUserId(
            Integer bookingStateId,
            Integer roomTypeId,
            Integer capacity,
            LocalDateTime offertimeInit,
            LocalDateTime offertimeEnd,
            int page,
            int size,Integer userId);

    Mono<Boolean> deleteBookingNotPay();

    Mono<BookingEntity> assignClientToBooking(Integer bookingId, Integer userClientId);

    Mono<Boolean> deleteBooking(Integer bookingId);

    Mono<Void> saveBookingWithFeedings(BookingFeedingDto requestDTO);
}
