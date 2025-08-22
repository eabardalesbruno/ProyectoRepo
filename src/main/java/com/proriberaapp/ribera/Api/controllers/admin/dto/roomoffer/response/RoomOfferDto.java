package com.proriberaapp.ribera.Api.controllers.admin.dto.roomoffer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomOfferDto {
    private Integer roomofferid;
    private String roomoffername;
}
