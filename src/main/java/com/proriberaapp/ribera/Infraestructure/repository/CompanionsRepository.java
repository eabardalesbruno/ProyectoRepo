package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CompanionsRepository extends R2dbcRepository<CompanionsEntity, Integer> {

    Flux<CompanionsEntity> findByBookingId(Integer bookingId);

    Mono<CompanionsEntity> findByBookingIdAndDocumentNumber(Integer bookingId, String documentNumber);

    Mono<CompanionsEntity> findByCompanionIdAndBookingId(Integer companionId, Integer bookingId);

}
