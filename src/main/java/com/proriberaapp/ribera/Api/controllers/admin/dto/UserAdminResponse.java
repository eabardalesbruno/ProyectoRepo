package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;

public record UserAdminResponse(
        Integer userAdminId,
        String email,
        String username,
        String firstName,
        String lastName
) {
    public static UserAdminResponse toResponse(UserAdminEntity users) {
        return new UserAdminResponse(
                users.getUserAdminId(),
                users.getEmail(),
                users.getUsername(),
                users.getFirstName(),
                users.getLastName()
        );
    }
}
