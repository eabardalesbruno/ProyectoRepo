package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.services.client.OfferTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/offer-type")
@RequiredArgsConstructor
public class ManagerOfferTypeController extends BaseManagerController<OfferTypeEntity, OfferTypeEntity> {
    private final OfferTypeService offerTypeService;

    @GetMapping
    public Flux<OfferTypeEntity> findAllOfferTypes() {
        return offerTypeService.findAll();
    }
}
