package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/comfort-room-offer-detail")
@RequiredArgsConstructor
public class ManagerComfortRoomOfferDetailController {
    private final ComfortRoomOfferDetailService comfortRoomOfferDetailService;

    @GetMapping("/find/all")
    public Flux<ComfortRoomOfferDetailEntity> getAllComfortRoomOfferDetails() {
        return comfortRoomOfferDetailService.findAll();
    }

    @GetMapping("/find")
    public Mono<ComfortRoomOfferDetailEntity> getComfortRoomOfferDetailById(@RequestParam Integer id) {
        return comfortRoomOfferDetailService.findById(id);
    }

    @PostMapping("/register")
    public Mono<ComfortRoomOfferDetailEntity> registerComfortRoomOfferDetail(
            @RequestBody ComfortRoomOfferDetailEntity comfortRoomOfferDetailEntity
    ) {
        return comfortRoomOfferDetailService.save(comfortRoomOfferDetailEntity);
    }

    @PostMapping("/register/all")
    public Flux<ComfortRoomOfferDetailEntity> registerAllComfortRoomOfferDetails(
            @RequestBody List<ComfortRoomOfferDetailEntity> comfortRoomOfferDetailEntity
    ) {
        return comfortRoomOfferDetailService.saveAll(comfortRoomOfferDetailEntity);
    }

    @PatchMapping("/update")
    public Mono<ComfortRoomOfferDetailEntity> updateComfortRoomOfferDetail(
            @RequestBody ComfortRoomOfferDetailEntity comfortRoomOfferDetailEntity
    ) {
        return comfortRoomOfferDetailService.update(comfortRoomOfferDetailEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteComfortRoomOfferDetail(Integer id) {
        return comfortRoomOfferDetailService.deleteById(id);
    }
}
