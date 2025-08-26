package com.proriberaapp.ribera.Api.controllers.admin.dto.bookingroomchanges.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingRoomChangeEmailResponseDto {
    private String imgurlnew;
    private String clientname;
    private String clientemail;
    private String roomnameold;
    private String roomnamenew;
    private Integer bookingid;
    private String checkin;
    private String checkout;
    private String totalnights;
    private String totalpeople;
}
