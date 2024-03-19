package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.OfferTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/offer-type")
@RequiredArgsConstructor
public class ManagerOfferTypeController {
    private final OfferTypeService offerTypeService;

    @GetMapping("/find/all")
    public Flux<OfferTypeEntity> getAllOfferTypes() {
        return offerTypeService.findAll();
    }

    @GetMapping("/find")
    public Mono<OfferTypeEntity> getOfferTypeById(
            @RequestParam Integer id) {
        return offerTypeService.findById(id);
    }

    @PostMapping("/register")
    public Mono<OfferTypeEntity> registerOfferType(
            @RequestBody OfferTypeEntity offerTypeEntity
    ) {
        return offerTypeService.save(offerTypeEntity);
    }

    @PostMapping("/register/all")
    public Flux<OfferTypeEntity> registerAllOfferTypes(
            @RequestBody List<OfferTypeEntity> offerTypeEntity
    ) {
        return offerTypeService.saveAll(offerTypeEntity);
    }

    @PatchMapping("/update")
    public Mono<OfferTypeEntity> updateOfferType(
            @RequestBody OfferTypeEntity offerTypeEntity
    ) {
        return offerTypeService.update(offerTypeEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deleteOfferType(Integer id) {
        return offerTypeService.deleteById(id);
    }
}
