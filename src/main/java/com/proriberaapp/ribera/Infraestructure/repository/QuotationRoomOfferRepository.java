package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface QuotationRoomOfferRepository extends ReactiveCrudRepository<QuotationRoomOfferEntity, Integer> {
    Flux<QuotationRoomOfferEntity> findAllByQuotationId(Integer quotationId);

    Flux<QuotationRoomOfferEntity> findAllByRoomOfferId(Integer roomOfferId);

    Mono<Void> deleteAllByRoomOfferId(Integer roomOfferId);

    Mono<Void> deleteAllByQuotationId(Integer quotationId);
}
