package com.proriberaapp.ribera.Api.controllers.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomDetailDto {

        Long roomid;
        Long roomnumber;
        Long roomofferid;
        String bookingstatename;

}
