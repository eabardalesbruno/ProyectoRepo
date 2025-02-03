package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Domain.entities.FeedingTypeFeedingGroupAndFeedingEntity;

import reactor.core.publisher.Mono;

public interface FeedingTypeFamilyGroupAndFeedingRepository
        extends ReactiveCrudRepository<FeedingTypeFeedingGroupAndFeedingEntity, Integer> {

    @Query("DELETE FROM feeding_type_feeding_family_group WHERE idfeeding = :idfeeding")
    Mono<Void> deleteByIdfeeding(Integer idfeeding);
}
