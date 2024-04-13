package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record PaymentResponse(
        String id,
        String name,
        String description,
        String price,
        String currency,
        String status
) {
}
