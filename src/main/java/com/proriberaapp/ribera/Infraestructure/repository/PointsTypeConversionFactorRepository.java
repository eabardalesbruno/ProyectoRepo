package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.PointQuotationRawDto;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorEntity;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTypeConversionFactorRepository
                extends R2dbcRepository<PointTypeConversionFactorEntity, Integer> {
        @Query("""
                SELECT
                    pt.*,
                    ptf.id,
                    ptf.costpernight,
                    ot.offertypeid,
                    ot.offertypename,
                    ptf.state AS status
                FROM
                    points_conversion_factor ptf
                JOIN
                    pointstype pt ON pt.pointstypeid = ptf.idpointtype
                JOIN
                    offertype ot ON ot.offertypeid = ptf.offerttypeid
                ORDER BY
                    pt.pointstypeid,
                    ot.offertypeid ASC;
                """)
        Flux<PointQuotationRawDto> getAllWithPointType();

}
