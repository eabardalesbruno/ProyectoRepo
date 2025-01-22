package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.QuotationDayEntity;
import com.proriberaapp.ribera.Domain.entities.QuotationRoomOfferEntity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface QuotationDayRepository extends ReactiveCrudRepository<QuotationDayEntity, Integer> {
    @Query("""
            delete from quotation_day where idquotation = :idQuotation
            """)
    Mono<Void> deleteFindQuotationId(Integer idQuotation);
}
