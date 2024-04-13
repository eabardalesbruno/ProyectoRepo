package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.TypeDocument;

import java.util.List;

public record UpdateUserAdminRequest(
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
}
