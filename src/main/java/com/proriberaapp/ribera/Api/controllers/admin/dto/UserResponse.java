package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;

public record UserResponse(
        Integer id,
        String email,
        String username,
        String firstName,
        String lastName,
        String document
) {

    public static UserResponse toResponse(UserAdminEntity users) {
        return new UserResponse(
                users.getUserAdminId(),
                users.getEmail(),
                users.getUsername(),
                users.getFirstName(),
                users.getLastName(),
                users.getDocument()
        );
    }

    public static UserResponse toResponsePromoter(UserPromoterEntity users) {
        return new UserResponse(
                users.getUserPromoterId(),
                users.getEmail(),
                users.getUsername(),
                users.getFirstName(),
                users.getLastName(),
                users.getDocument()
        );
    }

}
