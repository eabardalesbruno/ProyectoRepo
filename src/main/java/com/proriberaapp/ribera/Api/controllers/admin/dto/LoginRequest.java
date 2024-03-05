package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record LoginRequest(
        String username,
        String email,
        String password
) {
}
