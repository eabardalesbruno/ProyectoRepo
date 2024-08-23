package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStates {
    private String firstname;
    private String lastname;
    private Integer bookingid;
    private String roomtypename;
    private String email;
    private Double costfinal;
    private LocalDateTime daybookinginit;
    private LocalDateTime daybookingend;
    private Integer capacity;
    private String bookingstatename;
    private String bedtypename;
    private String bedtypedescription;
    private Integer riberapoints;
    private Integer inresortpoints;
    private Integer points;
}
