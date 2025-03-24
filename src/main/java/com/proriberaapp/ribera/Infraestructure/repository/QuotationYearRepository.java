package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.QuotationYearEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface QuotationYearRepository extends ReactiveCrudRepository<QuotationYearEntity, Integer> {
    @Query("""
            delete from quotation_year where id = :idQuotationYear
            """)
    Mono<Void> deleteFindQuotationYearId(Integer idQuotationYear);

    @Query("""
            select * from quotation_year where description = :year
            """)
    Mono<QuotationYearEntity> getQuotationYearByDescription(String year);
}
