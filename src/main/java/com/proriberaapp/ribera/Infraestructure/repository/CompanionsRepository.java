package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.CompanionsDto;
import com.proriberaapp.ribera.Domain.entities.CompanionsEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CompanionsRepository extends R2dbcRepository<CompanionsEntity, Integer> {

    Flux<CompanionsEntity> findByBookingId(Integer bookingId);

    Mono<CompanionsEntity> findByBookingIdAndDocumentNumber(Integer bookingId, String documentNumber);

    Mono<CompanionsEntity> findByCompanionIdAndBookingId(Integer companionId, Integer bookingId);

    @Query("""
     select c.*, dt.documenttypedesc, g.genderdesc,
       (select countrydesc from country where countryid = c.countryid) countrydesc,
       (SELECT courtesy FROM country WHERE countryid = c.countryid) AS courtesy
     from companions c
     left join documenttype dt on c.typedocumentid = dt.documenttypeid
     left join gender g on c.genderid = g.genderid
     where c.bookingId = :bookingId""")
    Flux<CompanionsDto> getCompanionsByBookingId(@Param("bookingId") Integer bookingId);

    @Query("SELECT * FROM companions WHERE fulldayid = :fulldayid")
    Flux<CompanionsEntity> findByFullDayId(Integer fulldayid);

}
