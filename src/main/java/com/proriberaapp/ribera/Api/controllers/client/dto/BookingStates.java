package com.proriberaapp.ribera.Api.controllers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStates {
    private String firstname;
    private String lastname;
    private Integer bookingid;
    private Integer roomtypeid;
    private String roomtypename;
    private String email;
    private Double costfinal;
    private LocalDateTime daybookinginit;
    private LocalDateTime daybookingend;
    private String image;
    private Integer capacity;
    private Integer bookingstateid;
    private String bookingstatename;
    private String bedtypename;
    private String bedtypedescription;
    private Integer riberapoints;
    private Integer inresortpoints;
    private Integer points;
    private Integer numberadults;
    private Integer numberchildren;
    private Integer numberbabies;
    private Integer numberadultsextra;
    private Integer numberadultsmayor;
    private LocalDateTime offertimeinit;
    private LocalDateTime offertimeend;
}
