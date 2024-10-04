package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.services.client.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comfort-room-offer-detail")
@RequiredArgsConstructor
public class ManagerComfortRoomOfferDetailController extends BaseManagerController<ComfortRoomOfferDetailEntity, ComfortRoomOfferDetailEntity> {
    private final ComfortRoomOfferDetailService comfortRoomOfferDetailService;

    @PostMapping
    public Flux<ComfortRoomOfferDetailEntity> saveComfortRoomOfferDetail(@RequestBody List<ComfortRoomOfferDetailEntity> comfortRoomOfferDetailEntity) {
        return comfortRoomOfferDetailService.saveAll(comfortRoomOfferDetailEntity);
    }

    @GetMapping("/room-offer/{roomOfferId}")
    public Flux<ComfortRoomOfferDetailEntity> findAllByRoomOfferId(@PathVariable Integer roomOfferId) {
        return comfortRoomOfferDetailService.findAllByRoomOfferId(roomOfferId);
    }

    @GetMapping("/count/{id}")
    public Mono<Integer> countByComfortTypeId(@PathVariable Integer id) {
        return comfortRoomOfferDetailService.countByComfortTypeId(id);
    }

}
