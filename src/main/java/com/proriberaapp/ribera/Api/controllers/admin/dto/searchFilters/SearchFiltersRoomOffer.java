package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

public record SearchFiltersRoomOffer(
        Integer roomId,
        Integer roomOfferId,
        Integer roomTypeId,

        String typeRoom
) {
}
