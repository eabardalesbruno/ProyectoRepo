package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.roomoffer.response.DropdownRoomOfferResponse;
import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${url.manager}/room-offer")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomOfferController extends BaseManagerController<RoomOfferEntity, RoomOfferEntity> {
    private final RoomOfferService roomOfferService;

    @GetMapping("/find/all/view")
    public Flux<ViewRoomOfferReturn> viewRoomOfferReturn(
            @Nullable @RequestParam Integer roomId,
            @Nullable @RequestParam Integer roomOfferId,
            @Nullable @RequestParam Integer roomTypeId,
            @Nullable @RequestParam String typeRoom
    ) {
        SearchFiltersRoomOffer filters = new SearchFiltersRoomOffer(roomId, roomOfferId, roomTypeId, typeRoom);
        return roomOfferService.viewRoomOfferReturn(filters);
    }

    @GetMapping("/dropdown")
    public Mono<DropdownRoomOfferResponse> getRoomOfferDropdown(@RequestParam(required = false) String searchTerm) {
        return roomOfferService.findDropdownRoomOffer(searchTerm);
    }
}
