package com.proriberaapp.ribera.Api.controllers.dto;

public class PaymentTokenResponse {
    private String token;

    public PaymentTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
