package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.util.Map;

public record SearchFiltersServiceAccommodation(
        Map<Integer, String> state,
        Map<Integer, String> typeRoom
) {
}
