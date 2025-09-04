package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response.BookingDetailDto;
import com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response.BookingDetailResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.FindFiltersBooking;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBooking;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBookingAvailability;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersBookingInventory;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.Infraestructure.viewRepository.BookingViewRepository;
import com.proriberaapp.ribera.services.admin.BookingManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingManagerServiceImpl implements BookingManagerService {

    private final BookingViewRepository bookingViewRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final StateRoomRepository stateRoomRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final BookingRepository bookingRepository;
    private final ComfortTypeRepository comfortTypeRepository;
    private final BedsTypeRepository bedsTypeRepository;
    private final UserClientRepository userClientRepository;

    @Override
    public Flux<ViewAdminBookingReturn> viewAdminBookingReturn(SearchFiltersBooking filters) {
        return bookingViewRepository.viewAdminBookingReturn(filters);
    }

    @Override
    public Flux<ViewAdminBookingInventoryReturn> viewAdminBookingInventoryReturn(SearchFiltersBookingInventory filters) {
        return bookingViewRepository.viewAdminBookingInventoryReturn(filters);
    }

    @Override
    public Flux<ViewAdminBookingAvailabilityReturn> viewAdminBookingAvailabilityReturn(SearchFiltersBookingAvailability filters) {
        return bookingViewRepository.viewAdminBookingAvailabilityReturn(filters);
    }

    @Override
    public Mono<FindFiltersBooking> findFiltersBooking() {
        return roomTypeRepository.findAll().collectList()
                .flatMap(roomType ->
                        stateRoomRepository.findAll().collectList()
                                .flatMap(stateRoom ->
                                        offerTypeRepository.findAll().collectList()
                                                .map(offerType -> FindFiltersBooking.builder()
                                                        .roomType(roomType)
                                                        .stateRoom(stateRoom)
                                                        .offerType(offerType)
                                                        .build()
                                                )
                                )
                );
    }

    @Override
    public Mono<ViewBookingReturn> getBooking(Integer id) {
        return bookingRepository.findAllViewBookingReturnByBookingId(id)
                .flatMap(viewBookingReturn ->
                        comfortTypeRepository.findAllByViewComfortType(viewBookingReturn.getBookingId())
                                .collectList().map(comfortTypeEntity -> {
                                    viewBookingReturn.setListComfortType(comfortTypeEntity);
                                    return viewBookingReturn;
                                })
                )
                .flatMap(viewBookingReturn ->
                        bedsTypeRepository.findAllByViewBedsType(viewBookingReturn.getBookingId())
                                .collectList().map(bedsTypeEntity -> {
                                    viewBookingReturn.setListBedsType(bedsTypeEntity);
                                    return viewBookingReturn;
                                })
                );
    }

    @Override
    public Mono<BookingDetailResponse> getBookingDetailByBookingId(Integer bookingId) {
        log.info("Inicio de metodo getBookingDetailByBookingId con bookingId:{}", bookingId);
        return bookingRepository.findBookingDetailByBookingId(bookingId)
                .flatMap(bookingDetailDto ->
                        userClientRepository.findUserDetailByUserClientId(bookingDetailDto.getUserclientid())
                                .map(userDetailDto -> {
                                    BookingDetailDto detailDto = BookingDetailDto.builder()
                                            .bookingid(bookingDetailDto.getBookingid())
                                            .costfinal(bookingDetailDto.getCostfinal())
                                            .daybookinginit(bookingDetailDto.getDaybookinginit())
                                            .daybookingend(bookingDetailDto.getDaybookingend())
                                            .roomofferid(bookingDetailDto.getRoomofferid())
                                            .roomnumber(bookingDetailDto.getRoomnumber())
                                            .roomname(bookingDetailDto.getRoomname())
                                            .imgurl(bookingDetailDto.getImgurl())
                                            .bookingstate(bookingDetailDto.getBookingstate())
                                            .checkin(bookingDetailDto.getCheckin())
                                            .checkout(bookingDetailDto.getCheckout())
                                            .totalnights(bookingDetailDto.getTotalnights())
                                            .totalpeople(bookingDetailDto.getTotalpeople())
                                            .userclientid(bookingDetailDto.getUserclientid())
                                            .user(userDetailDto)
                                            .build();
                                    return BookingDetailResponse.builder()
                                            .data(detailDto)
                                            .result(true)
                                            .build();
                                })
                )
                .switchIfEmpty(Mono.just(BookingDetailResponse.builder().result(false).data(null).build()))
                .onErrorResume(e -> {
                    log.error("Error al obtener detalle de booking | bookingId: {} | Error: {}", bookingId, e.getMessage(), e);
                    return Mono.just(BookingDetailResponse.builder().result(false).data(null).build());
                })
                .doOnSuccess(value -> log.info("Fin del método getBookingDetailByBookingId"));
    }
}
