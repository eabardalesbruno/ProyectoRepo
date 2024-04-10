package com.proriberaapp.ribera.Api.controllers.dto;

import java.sql.Timestamp;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        Integer registerTypeId,
        Integer userLevelId,
        Integer codeUser,
        Integer countryId,
        Integer genderId,
        Integer nationalityId,
        Integer areazoneId,
        String documentType,
        String documentNumber,
        Timestamp birthDate,
        Integer role,
        String civilStatus,
        String city,
        String address,
        String cellNumber,
        String googleAuth,
        String googleId,
        String googleEmail,
        String username
) {}
