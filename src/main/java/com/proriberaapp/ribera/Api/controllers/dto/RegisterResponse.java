package com.proriberaapp.ribera.Api.controllers.dto;

public record RegisterResponse(
        Integer userId,
        String firstName,
        String lastName
) {}
