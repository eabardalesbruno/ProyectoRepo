package com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.Domain.entities.RoomTypeEntity;
import com.proriberaapp.ribera.Domain.entities.StateRoomEntity;
import lombok.Builder;
import reactor.core.publisher.Flux;

import java.util.List;

@Builder
public record FindFiltersBooking(
        List<RoomTypeEntity> roomTypeEntity,
        List<StateRoomEntity> stateRoomEntity,
        List<OfferTypeEntity> offerTypeEntity
) {

}
