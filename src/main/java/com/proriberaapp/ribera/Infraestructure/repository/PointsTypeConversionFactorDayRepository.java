package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorDayEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTypeConversionFactorDayRepository
        extends R2dbcRepository<PointTypeConversionFactorDayEntity, Integer> {
    @Query("""
                select id from points_coversion_factor_day where idpointtype=:idpointtype
            """)
    Flux<PointTypeConversionFactorDayEntity> getDaysIgnoreIdFactorConversion(Integer idpointtype);

    @Query("""
                select id from points_coversion_factor_day where idpointtype=:idpointtype
                and idconversionfactor<>:id

            """)
    Flux<PointTypeConversionFactorDayEntity> getDaysIgnoreIdFactorConversion(Integer idpointtype, Integer id);

    @Query("""
                delete from points_coversion_factor_day where idconversionfactor=:idconversionfactor
            """)
    Mono<Void> deleteFindConversionFactoId(Integer idconversionfactor);

     @Query("""
            select d.*,(case when qd."id" is not null then true else false end) as selected from "day" d
            left join points_coversion_factor_day qd on qd.idday=d."id" and qd.idconversionfactor=:idconversionfactor
            order by d.id asc
            """)
    Flux<PointQuotationDayDto> getQuotationDaySelected(Integer idconversionfactor);


}
