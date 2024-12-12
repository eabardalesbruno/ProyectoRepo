package com.proriberaapp.ribera.utils.emails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class BookingEmailDto {
    private String roomName;
    private String clientName;
    private String code;
    private String dateCheckIn;
    private String dateCheckOut;
    private String hourCheckIn;
    private String imgSrc;
    private int days;
    private String location;
    private String cantidadPersonas;
}
