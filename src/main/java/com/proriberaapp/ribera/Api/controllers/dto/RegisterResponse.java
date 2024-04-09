package com.proriberaapp.ribera.Api.controllers.dto;

import java.sql.Timestamp;

public record RegisterResponse(
        Integer userId,
        String firstName,
        String lastName,
        Integer registerTypeId,
        Integer userLevelId,
        Integer countryId,
        Integer codeUser,
        String nationality,
        String documentType,
        String documentNumber,
        Timestamp birthDate,
        String sex,
        Integer role,
        String civilStatus,
        String city,
        String address,
        String cellNumber,
        String email,
        String googleAuth,
        String googleId,
        String googleEmail,
        String username
) {}
