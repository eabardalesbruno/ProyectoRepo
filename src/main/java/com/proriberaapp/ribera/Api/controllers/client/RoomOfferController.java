package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.searchFilters.SearchFiltersRoomOffer;
import com.proriberaapp.ribera.Api.controllers.admin.dto.views.ViewRoomOfferReturn;
import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.client.RoomOfferService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room-offer")
@RequiredArgsConstructor
public class RoomOfferController {
    private final RoomOfferService roomOfferService;

    @GetMapping("/find")
    public Mono<RoomOfferEntity> findRoomOffer(@RequestParam Integer id) {
        return roomOfferService.findById(id);
    }

    @GetMapping("{id}")
    public Mono<RoomOfferEntity> findRoomOfferById(@PathVariable Integer id) {
        return roomOfferService.findById(id);
    }
    @GetMapping("/detail/{id}")
    public Mono<ViewRoomOfferReturn> findDetailRoomOfferById(@PathVariable Integer id) {
        return roomOfferService.findRoomOfferById(id);
    }

    @GetMapping("/detail-roomoffer-with-quotation/{roomOfferId}")
    public Mono<ViewRoomOfferReturn> findDetailRoomOfferByIdAndQuotationId(@PathVariable Integer roomOfferId,
                                                                           @RequestParam(required = true) Integer quotationId) {
        return roomOfferService.findRoomOfferByIdAndQuotationId(roomOfferId,quotationId);
    }

    @GetMapping("/find/all")
    public Flux<RoomOfferEntity> findAllRoomOffers() {
        return roomOfferService.findAll();
    }

    @GetMapping("/find/all/view")
    public Flux<ViewRoomOfferReturn> viewRoomOfferReturn(
            @Nullable @RequestParam Integer roomId,
            @Nullable @RequestParam Integer roomOfferId,
            @Nullable @RequestParam Integer roomTypeId,
            @Nullable @RequestParam String typeRoom) {
        SearchFiltersRoomOffer filters = new SearchFiltersRoomOffer(roomId, roomOfferId, roomTypeId, typeRoom);
        return roomOfferService.viewRoomOfferReturn(filters);
    }

    @GetMapping("/filter-v2")
    public Flux<ViewRoomOfferReturn> getFilteredRoomOffersV2(
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) LocalDate offerTimeInit,
            @RequestParam(required = false) LocalDate offerTimeEnd,
            @RequestParam(required = false) Integer infantCapacity,
            @RequestParam(required = false) Integer kidCapacity,
            @RequestParam(required = false) Integer adultCapacity,
            @RequestParam(required = false) Integer adultMayorCapacity,
            @RequestParam(required = false) Integer adultExtra,
            @RequestParam(required = false) Boolean isFirstState,
            @RequestParam(required = false) List<Integer> feedings) {
        return roomOfferService.findFilteredV2(roomTypeId,
                categoryName, offerTimeInit, offerTimeEnd, kidCapacity, adultCapacity,
                adultMayorCapacity, adultExtra, infantCapacity, feedings, isFirstState);

    }

    @GetMapping("/filter")
    public Flux<ViewRoomOfferReturn> getFilteredRoomOffers(
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate offerTimeInit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate offerTimeEnd,
            @RequestParam(required = false) Integer infantCapacity,
            @RequestParam(required = false) Integer kidCapacity,
            @RequestParam(required = false) Integer adultCapacity,
            @RequestParam(required = false) Integer adultMayorCapacity,
            @RequestParam(required = false) Integer adultExtra) {

        return roomOfferService.findFiltered(roomTypeId, offerTimeInit, offerTimeEnd,
                infantCapacity, kidCapacity,
                adultCapacity, adultMayorCapacity, adultExtra);

    }

    @GetMapping("/getDepartments")
    public Flux<ViewRoomOfferReturn> getDepartments(
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate offerTimeInit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate offerTimeEnd,
            @RequestParam(required = false) Integer infantCapacity,
            @RequestParam(required = false) Integer kidCapacity,
            @RequestParam(required = false) Integer adultCapacity,
            @RequestParam(required = false) Integer adultMayorCapacity,
            @RequestParam(required = false) Integer adultExtra) {

        return roomOfferService.findDepartments(roomTypeId, offerTimeInit, offerTimeEnd, infantCapacity, kidCapacity,
                adultCapacity, adultMayorCapacity, adultExtra);
    }

    @PostMapping
    public Mono<RoomOfferEntity> saveRoomOffer(@RequestBody RoomOfferEntity roomOfferEntity) {
        return roomOfferService.save(roomOfferEntity);
    }

    @PutMapping("{id}")
    public Mono<RoomOfferEntity> updateRoomOffer(@PathVariable Integer id,
            @RequestBody RoomOfferEntity roomOfferEntity) {
        roomOfferEntity.setRoomOfferId(id);
        return roomOfferService.update(roomOfferEntity);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteRoomOffer(@PathVariable Integer id) {
        return roomOfferService.deleteById(id);
    }

}
