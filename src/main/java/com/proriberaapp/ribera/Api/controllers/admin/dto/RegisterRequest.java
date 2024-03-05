package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record RegisterRequest(
        String email,
        String password,
        String username,
        String firstName,
        String lastName
) {
}
