package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.dto.FeedingDto;
import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FeedingService {
    Flux<FeedingEntity> findAllFeeding();

    Mono<FeedingEntity> findFeedingById(Integer id);

    Mono<FeedingEntity> saveFeeding(FeedingDto feedingDto);

    Mono<FeedingEntity> updateFeeding(FeedingDto feedingDto);

    Mono<Void> deleteFeeding(Integer id);

    Flux<RoomOfferFeedingEntity> findRoomOfferByFeedingId(Integer feedingId);
}
