package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.util.Map;

public record SearchFiltersRoom(
        Map<Integer, String> typeRoom,
        Map<Integer, String> state
) {
}
