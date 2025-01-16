package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.Data;

@Data
public class BookingFeedingDto {
    private String name;
    private String description;
    private float cost;
    private Integer bookingId;

    
}
