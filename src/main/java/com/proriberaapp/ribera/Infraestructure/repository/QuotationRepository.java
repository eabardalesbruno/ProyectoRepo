package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends ReactiveCrudRepository<QuotationEntity, Integer> {
    @Query("""
            select d.*,(case when qd."id" is not null then true else false end) as selected from "day" d
            left join quotation_day qd on qd.idday=d."id" and qd.idquotation=:quotationId
            order by d.id asc
            """)
    Flux<quotationDayDto> getQuotationDaySelected(Integer quotationId);
}
