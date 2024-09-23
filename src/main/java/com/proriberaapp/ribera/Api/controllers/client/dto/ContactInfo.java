package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class ContactInfo {
    private String name;
    private String lastName;
    private String cellphone;
    private String documentNumber;
    private String subject;
    private String email;
    private String message;
}
