package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.States;
import com.proriberaapp.ribera.Domain.enums.TypeDocument;
import org.springframework.data.relational.core.mapping.Column;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String address,
        TypeDocument typeDocument,
        String document,
        Role role,
        List<Permission> permission
) {

    public static UserAdminEntity from(RegisterRequest registerRequest, String password) {
        return UserAdminEntity.builder()
                .email(registerRequest.email())
                .password(password)
                .username(registerRequest.firstName().toUpperCase() + " " + registerRequest.lastName().toUpperCase())
                .firstName(registerRequest.firstName().toUpperCase())
                .lastName(registerRequest.lastName().toUpperCase())
                .phone(registerRequest.phone())
                .address(registerRequest.address())
                .typeDocument(registerRequest.typeDocument())
                .document(registerRequest.document())
                .role(registerRequest.role())
                .status(States.ACTIVE)
                .permission(registerRequest.permission())

                .build();
    }

}
