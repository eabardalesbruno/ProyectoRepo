package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public record RegisterTypeRequest(
        String registerTypeName
) {
    public static List<RegisterTypeEntity> toEntity(List<RegisterTypeRequest> registerTypeRequest) {
        return registerTypeRequest.stream().map(RegisterTypeRequest::toEntity).toList();
    }

    public RegisterTypeEntity toEntity() {
        return RegisterTypeEntity.builder()
                .registerTypeName(registerTypeName)
                .build();
    }
}
