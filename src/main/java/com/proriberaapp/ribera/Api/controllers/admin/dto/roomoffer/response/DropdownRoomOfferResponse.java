package com.proriberaapp.ribera.Api.controllers.admin.dto.roomoffer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DropdownRoomOfferResponse {
    private boolean result;
    private List<RoomOfferDto> data;
}
