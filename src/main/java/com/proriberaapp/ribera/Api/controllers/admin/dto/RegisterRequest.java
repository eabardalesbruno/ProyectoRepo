package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.States;

import java.util.List;

public record RegisterRequest(
        String email,
        String password,
        String username,
        String firstName,
        String lastName,
        Role role,
        List<Permission> permission
) {

    public static UserAdminEntity from(RegisterRequest registerRequest, String password) {
        return UserAdminEntity.builder()
                .email(registerRequest.email())
                .password(password)
                .username(registerRequest.username())
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .role(registerRequest.role())
                .status(States.ACTIVE)
                .permission(registerRequest.permission())
                .build();
    }

}
