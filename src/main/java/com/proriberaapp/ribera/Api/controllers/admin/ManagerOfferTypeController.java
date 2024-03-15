package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.OfferTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/offers-type")
@RequiredArgsConstructor
public class ManagerOfferTypeController {
    private final OfferTypeService offerTypeService;

    @GetMapping("/find/all")
    public Flux<OfferTypeEntity> getAllOfferTypes() {
        return offerTypeService.findAll();
    }

    @GetMapping("/find")
    public Mono<OfferTypeEntity> getOfferTypeById(Integer id) {
        return offerTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<OfferTypeEntity> registerOfferType() {
        return offerTypeService.save(null);
    }

    @PostMapping("/register/all")
    public Flux<OfferTypeEntity> registerAllOfferTypes() {
        return offerTypeService.saveAll(null);
    }

    @PatchMapping("/update")
    public Mono<OfferTypeEntity> updateOfferType() {
        return offerTypeService.update(null);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteOfferType(Integer id) {
        return offerTypeService.deleteById(id);
    }
}
