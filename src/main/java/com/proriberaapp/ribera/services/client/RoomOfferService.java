package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOfferFiltro;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface RoomOfferService extends BaseService<RoomOfferEntity, RoomOfferEntity> {

    Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters);
    Flux<RoomOfferEntity> findByFilters(SearchFiltersRoomOfferFiltro filters);
    Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDateTime offerTimeInit, LocalDateTime offerTimeEnd, Integer capacity);

}
