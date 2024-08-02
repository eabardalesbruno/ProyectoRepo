package com.proriberaapp.ribera.Api.controllers.client.dto;

public class TokenResult {
    private String token;
    private String message;

    public TokenResult(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
