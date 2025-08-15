package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.roomoffer.response.DropdownRoomOfferResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOfferFiltro;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.BaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface RoomOfferService extends BaseService<RoomOfferEntity, RoomOfferEntity> {

        Flux<ViewRoomOfferReturn> viewRoomOfferReturn(SearchFiltersRoomOffer filters);

        Flux<RoomOfferEntity> findByFilters(SearchFiltersRoomOfferFiltro filters);

        Flux<ViewRoomOfferReturn> findFilteredV2(Integer roomTypeId, String categoryName, LocalDate offerTimeInit,
                        LocalDate offerTimeEnd,
                        Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
                        Integer adultExtraCapacity, Integer infantCapacity, List<Integer> feedingsSelected,
                        boolean isFirstState);

        Flux<ViewRoomOfferReturn> findFiltered(Integer roomTypeId, LocalDate offerTimeInit,
                        LocalDate offerTimeEnd,
                        Integer infantCapacity, Integer kidCapacity, Integer adultCapacity, Integer adultMayorCapacity,
                        Integer adultExtra);

        Flux<ViewRoomOfferReturn> findDepartments(Integer roomTypeId, LocalDate offerTimeInit, LocalDate offerTimeEnd,
                                                  Integer infantCapacity, Integer kidCapacity, Integer adultCapacity,
                                                  Integer adultMayorCapacity, Integer adultExtra);

        Mono<ViewRoomOfferReturn> findRoomOfferById(Integer id);

        Mono<ViewRoomOfferReturn> findRoomOfferByIdAndQuotationId(Integer roomOfferId, Integer quotationId);

        Mono<DropdownRoomOfferResponse> findDropdownRoomOffer(String searchTerm);
}
