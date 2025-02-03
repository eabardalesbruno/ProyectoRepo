package com.proriberaapp.ribera.Api.controllers.admin;

import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JWSAlgorithm.Family;
import com.proriberaapp.ribera.Domain.entities.FamilyGroupEntity;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.FeedingTypeEntity;
import com.proriberaapp.ribera.Domain.entities.FeedingTypeFeedingGroupAndFeedingEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FeedingTypeFamilyGroupAndFeedingRepository;

import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/maintenance/feeding-items")
public class FeedingItemsController {
    @Autowired
    private FeedingTypeFamilyGroupAndFeedingRepository repository;

    @GetMapping("/{idFeeding}")
    public Flux<FeedingTypeFeedingGroupAndFeedingEntity> getMethodName(@PathVariable Integer idFeeding) {
        return this.repository.findByIdfeeding(idFeeding).flatMap(d -> {
            FeedingEntity feeding = FeedingEntity.builder().id(d.getIdfeeding()).name(d.getFeedingname()).build();
            FamilyGroupEntity familyGroup = FamilyGroupEntity.builder()
                    .id(d.getIdfamilygroup())
                    .name(d.getFamilygroupname())
                    .build();
            FeedingTypeEntity feedingType = FeedingTypeEntity.builder()
                    .id(d.getIdfeedingtype())
                    .name(d.getFeedingtypename())
                    .build();
            FeedingTypeFeedingGroupAndFeedingEntity entity = FeedingTypeFeedingGroupAndFeedingEntity.builder()
                    .feeding(feeding)
                    .familyGroup(familyGroup)
                    .feedingType(feedingType)
                    .value(d.getValue())
                    .build();
            return Flux.just(entity);
        });
    }

}
