package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.QuotationOfferDayDto;
import com.proriberaapp.ribera.Api.controllers.client.dto.quotationDayDto;
import com.proriberaapp.ribera.Domain.entities.QuotationEntity;

import reactor.core.publisher.Flux;

import java.util.List;

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

    @Query("""
                    select q.quotation_description,d."name" as dayname,r.offername from quotation q
            join quotation_roomoffer qr on qr.quotation_id=q.quotation_id
            join roomoffer r on r.roomofferid=qr.room_offer_id
            join quotation_day qd on qd.idquotation=qd.idquotation
            join "day" d on d."id"=qd.idday
            where 
             qd.idday in(:dayId)
            					 and qr.room_offer_id in(:roomOfferId)
            and qr.quotation_id<>:quotationIdIgnore

                    """)
    Flux<QuotationOfferDayDto> getQuotationFindOfferAndDays(List<Integer> roomOfferId, List<Integer> dayId,
            Integer quotationIdIgnore);

    @Query("""
                    select q.quotation_description,d."name" as dayname,r.offername from quotation q
            join quotation_roomoffer qr on qr.quotation_id=q.quotation_id
            join roomoffer r on r.roomofferid=qr.room_offer_id
            join quotation_day qd on qd.idquotation=qd.idquotation
            join "day" d on d."id"=qd.idday
            where qr.room_offer_id in(:roomOfferId)
            and d."id" in(:dayId)
                    """)
    Flux<QuotationOfferDayDto> getQuotationFindOfferAndDays(List<Integer> roomOfferId, List<Integer> dayId);

}
