package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record FindFiltersBooking(
        List<RoomTypeEntity> roomType,
        List<StateRoomEntity> stateRoom,
        List<OfferTypeEntity> offerType
) {

}
