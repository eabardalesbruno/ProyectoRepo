package com.proriberaapp.ribera.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.*;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingAvailabilityReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingInventoryReturn;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewAdminBookingReturn;
import com.proriberaapp.ribera.Api.controllers.client.dto.ViewBookingReturn;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingManagerService {

    Flux<ViewAdminBookingReturn> viewAdminBookingReturn(SearchFiltersBooking filters);

    Flux<ViewAdminBookingInventoryReturn> viewAdminBookingInventoryReturn(SearchFiltersBookingInventory filters);

    Flux<ViewAdminBookingAvailabilityReturn> viewAdminBookingAvailabilityReturn(SearchFiltersBookingAvailability filters);

    Mono<FindFiltersBooking> findFiltersBooking();

    Mono<ViewBookingReturn> getBooking(Integer id);
}
