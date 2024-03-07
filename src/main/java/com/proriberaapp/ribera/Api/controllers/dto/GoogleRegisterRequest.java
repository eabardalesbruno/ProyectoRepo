package com.proriberaapp.ribera.Api.controllers.dto;

public record GoogleRegisterRequest(
        String googleId,
        String email,
        String name
) {}
