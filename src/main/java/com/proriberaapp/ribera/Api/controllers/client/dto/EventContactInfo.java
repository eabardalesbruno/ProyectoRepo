package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class EventContactInfo {
    private String name;
    private String lastName;
    private String cellphone;
    private String email;
    private String startDate;
    private String endDate;
    private String numberAdults;
    private String numberChildren;
    private String message;
}