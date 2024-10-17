package com.proriberaapp.ribera.services.client;


import com.proriberaapp.ribera.Domain.entities.FeedingEntity;
import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoomOfferFeedingService {
    Flux<RoomOfferFeedingEntity> findAllRoomOfferFeeding();

    Mono<RoomOfferFeedingEntity> findRoomOfferFeedingById(Integer id);

    Mono<RoomOfferFeedingEntity> saveRoomOfferFeeding(RoomOfferFeedingEntity roomOfferFeeding);

    Mono<Void> deleteRoomOfferFeeding(Integer id);
}