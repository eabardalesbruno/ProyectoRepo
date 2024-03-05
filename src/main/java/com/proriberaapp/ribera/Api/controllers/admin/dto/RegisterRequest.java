package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;

public record RegisterRequest(
        String email,
        String password,
        String username,
        String firstName,
        String lastName
) {

    public static UserAdminEntity from(RegisterRequest registerRequest) {
        UserAdminEntity userAdminEntity = new UserAdminEntity();
        userAdminEntity.setEmail(registerRequest.email());
        userAdminEntity.setPassword(registerRequest.password());
        userAdminEntity.setUsername(registerRequest.username());
        userAdminEntity.setFirstName(registerRequest.firstName());
        userAdminEntity.setLastName(registerRequest.lastName());
        return userAdminEntity;
    }

}
