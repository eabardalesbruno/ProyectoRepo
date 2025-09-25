package com.proriberaapp.ribera.Api.controllers.admin.dto.bookingcancellations.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingCancellationEmailResponseDto {
    private String clientname;
    private String clientemail;
    private String roomname;
    private Integer bookingid;
    private String checkin;
    private String checkout;
    private String totalnights;
    private String totalpeople;
}
