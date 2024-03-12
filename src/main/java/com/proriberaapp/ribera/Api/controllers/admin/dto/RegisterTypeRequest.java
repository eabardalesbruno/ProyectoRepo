package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;

public record RegisterTypeRequest(
        String registerTypeName
) {
    public RegisterTypeEntity toEntity() {
        return RegisterTypeEntity.builder()
                .registerTypeName(registerTypeName)
                .build();
    }
}
