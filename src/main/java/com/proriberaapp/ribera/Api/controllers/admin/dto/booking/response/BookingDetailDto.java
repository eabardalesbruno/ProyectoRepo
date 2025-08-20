package com.proriberaapp.ribera.Api.controllers.admin.dto.booking.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingDetailDto {
    private Integer bookingid;
    private BigDecimal costfinal;
    private Integer roomofferid;
    private String roomnumber;
    private String roomname;
    private String imgurl;
    private String bookingstate;
    private String checkin;
    private String checkout;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime daybookinginit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime daybookingend;
    private String totalnights;
    private String totalpeople;
    private Integer userclientid;
    private UserDetailDto user;
}
