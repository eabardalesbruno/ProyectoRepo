package com.proriberaapp.ribera.Api.controllers.admin.dto;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import com.proriberaapp.ribera.Domain.entities.UserPromoterEntity;
import com.proriberaapp.ribera.Domain.enums.Permission;
import com.proriberaapp.ribera.Domain.enums.Role;
import com.proriberaapp.ribera.Domain.enums.StatesUser;
import com.proriberaapp.ribera.Domain.enums.TypeDocument;

import java.sql.Timestamp;
import java.util.List;

public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String address,
        Integer typeDocument,
        String document,
        Role role,
        List<Permission> permission,
        String googleAuth,
        String googleId,
        String googleEmail,
        Integer countryId,
        Integer genderId,
        Timestamp birthDate,
        String city,
        String username,
        Timestamp createdat
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
                .documenttypeId(registerRequest.typeDocument())
                .documentNumber(registerRequest.document())
                .role(registerRequest.role())
                .status(StatesUser.ACTIVE)
                .permission(registerRequest.permission())

                .build();
    }

    public static UserPromoterEntity fromPromoter(RegisterRequest registerRequest, String password) {
        return UserPromoterEntity.builder()
                .email(registerRequest.email())
                .password(password)
                .username(registerRequest.firstName().toUpperCase() + " " + registerRequest.lastName().toUpperCase())
                .firstName(registerRequest.firstName().toUpperCase())
                .lastName(registerRequest.lastName().toUpperCase())
                .phone(registerRequest.phone())
                .address(registerRequest.address())
                .documenttypeId(registerRequest.typeDocument())
                .documentNumber(registerRequest.document())
                .role(registerRequest.role())
                .status(StatesUser.ACTIVE)
                .permission(registerRequest.permission())
                .googleEmail(registerRequest.googleEmail())
                .googleId(registerRequest.googleId())
                .googleAuth(registerRequest.googleAuth())
                .build();
    }

}
