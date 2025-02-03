package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Domain.entities.FeedingTypeFeedingGroupAndFeedingEntity;

public interface FeedingTypeFamilyGroupAndFeedingRepository extends ReactiveCrudRepository<FeedingTypeFeedingGroupAndFeedingEntity, Integer> {
    
}
