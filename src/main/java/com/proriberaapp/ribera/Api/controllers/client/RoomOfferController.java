package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/room-offer")
@RequiredArgsConstructor
public class RoomOfferController {
    private final RoomOfferService roomOfferService;
    @GetMapping("/find")
    public Mono<RoomOfferEntity> findRoomOffer(@RequestParam Integer id) {
        return roomOfferService.findById(id);
    }
    @GetMapping("/find/all")
    public Flux<RoomOfferEntity> findAllRoomOffers() {
        return roomOfferService.findAll();
    }
}
