package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FeedingService {
    Flux<FeedingEntity> findAllFeeding();

    Mono<FeedingEntity> findFeedingById(Integer id);

    Mono<FeedingEntity> saveFeeding(FeedingEntity feeding);

    Mono<FeedingEntity> updateFeeding(FeedingEntity feeding);

    Mono<Void> deleteFeeding(Integer id);
}
