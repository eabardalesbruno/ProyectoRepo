package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RoomOfferFeedingEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoomOfferFeedingRepository extends ReactiveCrudRepository<RoomOfferFeedingEntity, Integer> {
    Mono<Void> deleteByFeedingId(Integer feedingId);
    Flux<RoomOfferFeedingEntity> findByFeedingId(Integer feedingId);
}
