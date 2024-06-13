package com.proriberaapp.ribera.Api.controllers.admin.dto;


import lombok.Builder;

@Builder
public record BedroomReturn (
        String bedTypeName,
        Integer quantity
) {}
