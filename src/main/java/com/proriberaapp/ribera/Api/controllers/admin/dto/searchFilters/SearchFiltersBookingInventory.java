package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.util.Map;

public record SearchFiltersBookingInventory(
        Integer roomId, Integer roomOfferId, Integer roomTypeId, Integer bookingId, Integer bookingStateId,
        Integer userClientId, String dateCreate, String numberBooking, String typeRoom, String client,
        String numberDocument, String costTotal
) {
}
