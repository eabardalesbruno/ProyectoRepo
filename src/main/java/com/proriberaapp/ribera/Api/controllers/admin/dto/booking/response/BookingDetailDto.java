package com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingDetailDto {
    private Integer bookingid;
    private Integer roomofferid;
    private String roomnumber;
    private String roomname;
    private String imgurl;
    private String bookingstate;
    private String checkin;
    private String checkout;
    private String totalnights;
    private String totalpeople;
    private Integer userclientid;
    private UserDetailDto user;
}
