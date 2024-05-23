package com.proriberaapp.ribera.services.admin.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.*;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Infraestructure.repository.OfferTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.RoomTypeRepository;
import com.proriberaapp.ribera.Infraestructure.repository.StateRoomRepository;
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
                                                        .roomTypeEntity(roomType)
                                                        .stateRoomEntity(stateRoom)
                                                        .offerTypeEntity(offerType)
                                                        .build()
                                                )
                                )
                );
    }

}
