package com.proriberaapp.ribera.Api.controllers.admin.dto;

public record UpdatePasswordRequest(
        Integer id,
        String newPassword
) {
}
