package com.proriberaapp.ribera.Api.controllers.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {}
