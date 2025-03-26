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
            SELECT DISTINCT q.*
            FROM quotation q
            JOIN quotation_day qd ON q.quotation_id = qd.idquotation
            WHERE (
                (:condition = 0 AND qd.idday IN (SELECT id FROM day WHERE name IN ('Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado')))
                OR
                (:condition = 1 AND qd.idday IN (SELECT id FROM day WHERE name IN ('Viernes', 'Sabado')))
                OR
                (:condition = 2 AND qd.idday IN (SELECT id FROM day WHERE name IN ('Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves')))
            )
            ORDER BY q.quotation_id ASC
            """)
    Flux<QuotationEntity> getAllQuotationByDays(Integer condition);

    @Query("""
            select d.*,(case when qd."id" is not null then true else false end) as selected, qy.id idyear
            from "day" d
            left join quotation_day qd on qd.idday=d."id" and qd.idquotation=:quotationId
            left join quotation_year qy on qy.id = qd.idyear
            order by d.id asc
            """)
    Flux<quotationDayDto> getQuotationDaySelected(Integer quotationId);

    @Query("""
                       select  q.quotation_description,d."name" as dayname,r.offername from quotation q
                                    join quotation_roomoffer qr on qr.quotation_id=q.quotation_id and  qr.room_offer_id in(:roomOfferId)
                            join quotation_day qd on qd.idquotation=q.quotation_id and qd.idday in(:dayId)
                            join "day" d on d."id"=qd.idday
                                    join roomoffer r on r.roomofferid=qr.room_offer_id
                            where qr.quotation_id<>:quotationIdIgnore

                    """)
    Flux<QuotationOfferDayDto> getQuotationFindOfferAndDays(List<Integer> roomOfferId, List<Integer> dayId,
            Integer quotationIdIgnore);

    @Query("""
            
                    select  q.quotation_description,d."name" as dayname,r.offername from quotation q
                            join quotation_roomoffer qr on qr.quotation_id=q.quotation_id and  qr.room_offer_id in(:roomOfferId)
                    join quotation_day qd on qd.idquotation=q.quotation_id and qd.idday in(:dayId)
                    join "day" d on d."id"=qd.idday
                    join roomoffer r on r.roomofferid=qr.room_offer_id
                    left join quotation_year qy on qy.id = qd.idyear and qy.id = :yearId
                    """)
    Flux<QuotationOfferDayDto> getQuotationFindOfferAndDays(Integer yearId, List<Integer> roomOfferId, List<Integer> dayId);

    @Query("""
        SELECT q.quotation_id, q.kid_reward, q.adult_reward, q.adult_mayor_reward, q.adult_extra_reward, qd.idday
        FROM quotation q
        JOIN quotation_roomoffer qr ON qr.quotation_id = q.quotation_id
        JOIN quotation_day qd ON qd.idquotation = q.quotation_id
        WHERE qr.room_offer_id = :roomOfferId
        AND qd.idday IN (:dayIds)
    """)
    Flux<QuotationEntity> findQuotationByRoomOfferAndDays(Integer roomOfferId, List<Integer> dayIds);

}
