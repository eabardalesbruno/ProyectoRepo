package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record ListAmenities(
        String comforttypename,
        String comforttypedescription,
        Integer quantity,
        Boolean active
) {
}
