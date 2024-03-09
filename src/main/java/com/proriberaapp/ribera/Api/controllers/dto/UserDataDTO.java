package com.proriberaapp.ribera.Api.controllers.dto;

public class UserDataDTO {
    private String username;
    private String documentNumber;
    private String civilStatus;
    private String email;
    private String city;
    private String cellNumber;
    private String birthDate;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getters y Setters
}