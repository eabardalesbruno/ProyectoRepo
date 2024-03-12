package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Infraestructure.services.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/partner-points")
@RequiredArgsConstructor
public class ManagerPartnerPointsController {
    private final PartnerPointsService partnerPointsService;

    @PostMapping("/register")
    public Mono<PartnerPointsEntity> registerPartnerPoints(
            @RequestBody PartnerPointsEntity partnerPointsEntity
    ) {
        return partnerPointsService.save(partnerPointsEntity);
    }

    @PostMapping("/register/all")
    public Flux<PartnerPointsEntity> registerAllPartnerPoints(
            @RequestBody Flux<PartnerPointsEntity> partnerPointsEntity
    ) {
        return partnerPointsService.saveAll(partnerPointsEntity);
    }

    @PatchMapping("/update")
    public Mono<PartnerPointsEntity> updatePartnerPoints(
            @RequestBody PartnerPointsEntity partnerPointsEntity
    ) {
        return partnerPointsService.update(partnerPointsEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deletePartnerPoints(
            @RequestParam String id
    ) {
        return partnerPointsService.deleteById(id);
    }

    @GetMapping("/find")
    public Mono<PartnerPointsEntity> findPartnerPoints(
            @RequestParam String id
    ) {
        return partnerPointsService.findById(id);
    }

    @GetMapping("/find/all")
    public Flux<PartnerPointsEntity> findAllPartnerPoints() {
        return partnerPointsService.findAll();
    }
}
