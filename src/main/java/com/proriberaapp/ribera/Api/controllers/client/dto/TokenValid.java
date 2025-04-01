package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;

import lombok.Getter;

@Getter
public class TokenValid {
    private final String value;
    private final String tokenBackOffice;

    public TokenValid(String value, String tokenBackOffice) {

        this.value = value;
        this.tokenBackOffice = tokenBackOffice;
    }

}
