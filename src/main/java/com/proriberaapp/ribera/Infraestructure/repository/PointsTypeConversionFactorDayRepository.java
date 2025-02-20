package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Api.controllers.client.dto.PointQuotationDayDto;
import com.proriberaapp.ribera.Domain.dto.PointGroupWithOffertRowDto;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorDayEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTypeConversionFactorDayRepository
                extends R2dbcRepository<PointTypeConversionFactorDayEntity, Integer> {
        @Query("""
                                    select fd.id from points_coversion_factor_day fd
                                     join points_conversion_factor cf on cf.id=fd.idconversionfactor  and  cf.offerttypeid=:offerTypeId
                                    where fd.idpointtype=:idpointtype
                                                and idday in(:daysSelected)
                                           

                        """)
        Flux<PointTypeConversionFactorDayEntity> getDaysIgnoreIdFactorConversion(Integer  idpointtype,
                        List<Integer> daysSelected, Integer offerTypeId);

        @Query("""
                            select id from points_coversion_factor_day where idpointtype<>:idpointtype
                            and idconversionfactor<>:id
                            and idday in(:daysSelected)

                        """)
        Flux<PointTypeConversionFactorDayEntity> getDaysIgnoreIdFactorConversion(Integer idpointtype, Integer id,
                        List<Integer> daysSelected);

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


        @Query("""
                select  sum(pcf.costpernight) as point ,pt.pointstypeid,pt.pointstypedesc,pcf.offerttypeid  from points_conversion_factor pcf
                   join points_coversion_factor_day pcfd on pcfd.idconversionfactor=pcf."id"
                    join "day" d on d."id"=pcfd.idday
                   join pointstype pt on pt.pointstypeid=pcf.idpointtype
                    where d.numberofweek in(  SELECT
                          (EXTRACT(DOW FROM fecha_inicio::date)+1) as dow
                      FROM generate_series(
                        :startDate::date,
                        :endDate::date-1,
                        '1 day'::interval
                      ) fecha_inicio)
                      GROUP BY pt.pointstypeid,pt.pointstypedesc ,pcf.offerttypeid
                """)
        Flux<PointGroupWithOffertRowDto> getTotalPointWithRangeDateSelected(LocalDate startDate, LocalDate endDate);




}
