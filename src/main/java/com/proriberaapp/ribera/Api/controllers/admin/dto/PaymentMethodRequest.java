package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;

public record PaymentMethodRequest(
    String description
) {
    public PaymentMethodEntity toEntity() {
        return PaymentMethodEntity.builder()
            .description(description)
            .build();
    }
}
