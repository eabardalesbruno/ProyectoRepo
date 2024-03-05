package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record UserAdminResponse(
        Integer userAdminId,
        String email,
        String username,
        String firstName,
        String lastName
) {
}
