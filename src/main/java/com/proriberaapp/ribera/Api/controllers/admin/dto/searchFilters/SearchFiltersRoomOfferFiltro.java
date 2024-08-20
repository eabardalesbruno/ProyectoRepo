package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.time.LocalDateTime;

public record SearchFiltersRoomOfferFiltro(
        Integer roomId,
        Integer roomOfferId,
        Integer roomTypeId,

        String typeRoom,
        LocalDateTime offerTimeInit,
        LocalDateTime offerTimeEnd,
        Integer capacity) {
}
