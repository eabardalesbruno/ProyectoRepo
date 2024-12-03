package com.proriberaapp.ribera.Api.controllers.client.dto;

import com.proriberaapp.ribera.Api.controllers.exception.TokenInvalidException;
import com.proriberaapp.ribera.Crosscutting.security.JwtProvider;

import lombok.Getter;

@Getter
public class TokenValid {
    private String value;

    public TokenValid(String value) {
        JwtProvider jwtProvider = new JwtProvider();
        if (!jwtProvider.validateToken(value))
            new TokenInvalidException();
        this.value = value;
    }

}
