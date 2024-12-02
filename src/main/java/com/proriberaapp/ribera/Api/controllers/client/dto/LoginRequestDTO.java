package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginRequestDTO {
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters y Setters
}