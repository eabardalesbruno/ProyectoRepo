package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.time.LocalDateTime;

public record SearchFiltersRoomOffer(
        Integer roomId,
        Integer roomOfferId,
        Integer roomTypeId,

        String typeRoom
) {
}
