package com.proriberaapp.ribera.Api.controllers.dto;

import java.sql.Timestamp;

public record RegisterResponse(
        Integer userClientId,
        String firstName,
        String lastName,
        Integer registerTypeId,
        Integer userLevelId,
        Integer countryId,
        Integer codeUser,
        Integer genderId,
        Integer documenttypeId,
        String documentNumber,
        Timestamp birthDate,
        Integer role,
        String civilStatus,
        String city,
        String address,
        String cellNumber,
        String email,
        String googleAuth,
        String googleId,
        String googleEmail,
        String username,
        Timestamp createdat

) {}
