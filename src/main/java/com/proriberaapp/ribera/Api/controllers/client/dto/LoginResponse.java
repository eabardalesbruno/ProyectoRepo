package com.proriberaapp.ribera.Api.controllers.client.dto;

public record LoginResponse(
        String token,
        String tokenBackOffice,
        String message
) {}
