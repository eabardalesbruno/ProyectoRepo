package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.Infraestructure.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/room-offer")
@RequiredArgsConstructor
@Slf4j
public class ManagerRoomOfferController {
    private final RoomOfferService roomOfferService;

    @GetMapping("/find/all")
    public Flux<RoomOfferEntity> getAllRoomOffers() {
        return roomOfferService.findAll();
    }

    @GetMapping("/find")
    public Mono<RoomOfferEntity> getRoomOfferById(Integer id) {
        return roomOfferService.findById(id);
    }

    @PostMapping("/register")
    public Mono<RoomOfferEntity> registerRoomOffer() {
        return roomOfferService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<RoomOfferEntity> registerAllRoomOffers() {
        return roomOfferService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<RoomOfferEntity> updateRoomOffer() {
        return roomOfferService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteRoomOffer(Integer id) {
        return roomOfferService.deleteById(id);
    }
}
