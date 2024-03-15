package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import com.proriberaapp.ribera.Infraestructure.services.ComfortRoomOfferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<ComfortRoomOfferDetailEntity> getComfortRoomOfferDetailById(Integer id) {
        return comfortRoomOfferDetailService.findById(id);
    }

    @PostMapping("/register")
    public Mono<ComfortRoomOfferDetailEntity> registerComfortRoomOfferDetail() {
        return comfortRoomOfferDetailService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<ComfortRoomOfferDetailEntity> registerAllComfortRoomOfferDetails() {
        return comfortRoomOfferDetailService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<ComfortRoomOfferDetailEntity> updateComfortRoomOfferDetail() {
        return comfortRoomOfferDetailService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteComfortRoomOfferDetail(Integer id) {
        return comfortRoomOfferDetailService.deleteById(id);
    }
}
