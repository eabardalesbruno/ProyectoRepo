package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailDto {

    String roomnumber;
    Long roomid;
    Long roomofferid;
    LocalDateTime daybookingend;
    Long paymentstateid;
    String paymentstatename;
    String roomStatus;
    Long bookingid;
    Integer numdays;
    String bookingstate;

}
