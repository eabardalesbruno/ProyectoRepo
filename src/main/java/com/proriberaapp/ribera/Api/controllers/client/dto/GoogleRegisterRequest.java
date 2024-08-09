package com.proriberaapp.ribera.Api.controllers.client.dto;

public record GoogleRegisterRequest(
        String googleId,
        String email,
        String name
) {}
