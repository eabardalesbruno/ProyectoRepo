package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import reactor.core.publisher.Flux;

public record RegisterTypeRequest(
        String registerTypeName
) {
    public static Flux<RegisterTypeEntity> toEntity(Flux<RegisterTypeRequest> registerTypeRequest) {
        return registerTypeRequest.map(RegisterTypeRequest::toEntity);
    }

    public RegisterTypeEntity toEntity() {
        return RegisterTypeEntity.builder()
                .registerTypeName(registerTypeName)
                .build();
    }
}
