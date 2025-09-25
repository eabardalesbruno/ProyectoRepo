package com.proriberaapp.ribera.Api.controllers.admin.dto;


import lombok.Builder;
import org.springframework.data.relational.core.mapping.Column;

@Builder
public record BedroomReturn (
        @Column("bedtypename")
        String bedTypeName,
        Integer quantity
) {}
