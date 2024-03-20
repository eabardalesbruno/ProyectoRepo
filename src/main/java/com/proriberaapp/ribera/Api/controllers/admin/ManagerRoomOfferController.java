package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.RoomOfferEntity;
import com.proriberaapp.ribera.services.RoomOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<RoomOfferEntity> registerRoomOffer(
            @RequestBody RoomOfferEntity roomOfferEntity
    ) {
        return roomOfferService.save(roomOfferEntity);
    }

    @PostMapping("/register/all")
    public Flux<RoomOfferEntity> registerAllRoomOffers(
            @RequestBody List<RoomOfferEntity> roomOfferEntity
    ) {
        return roomOfferService.saveAll(roomOfferEntity);
    }

    @PatchMapping("/update")
    public Mono<RoomOfferEntity> updateRoomOffer(
            @RequestBody RoomOfferEntity roomOfferEntity
    ) {
        return roomOfferService.update(roomOfferEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteRoomOffer(Integer id) {
        return roomOfferService.deleteById(id);
    }
}
