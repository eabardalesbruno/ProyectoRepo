package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.enums.TypeDocument;

public record RequestUpdateUserAdminRequest(
        String email,
        String phone,
        TypeDocument typeDocument,
        String document
) {
}
