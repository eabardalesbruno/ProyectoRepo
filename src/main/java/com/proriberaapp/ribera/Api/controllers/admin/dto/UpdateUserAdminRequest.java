package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record UpdateUserAdminRequest(
        String email,
        String username,
        String firstName,
        String lastName
) {
}
