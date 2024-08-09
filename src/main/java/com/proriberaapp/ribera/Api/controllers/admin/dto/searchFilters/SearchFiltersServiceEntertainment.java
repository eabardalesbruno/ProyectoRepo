package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import java.util.Map;

public record SearchFiltersServiceEntertainment(
        Map<Integer, String> category,
        Map<Integer, String> nameActivity
) {
}
