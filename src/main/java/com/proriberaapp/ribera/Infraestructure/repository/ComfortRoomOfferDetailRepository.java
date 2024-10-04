package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ComfortRoomOfferDetailEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ComfortRoomOfferDetailRepository extends R2dbcRepository<ComfortRoomOfferDetailEntity, Integer> {
    Flux<ComfortRoomOfferDetailEntity> findAllByRoomOfferId(Integer roomOfferId);

    Mono<Void> deleteAllByRoomOfferId(Integer roomOfferId);

    @Query("select SUM(quantity) FROM comfortroomofferdetail where comfortTypeId = :comfortTypeId GROUP BY comfortTypeId")
    Mono<Integer> countAllByComfortTypeId(Integer comfortTypeId);

    Flux<ComfortRoomOfferDetailEntity> findAllByComfortTypeId(Integer comfortTypeId);
}
