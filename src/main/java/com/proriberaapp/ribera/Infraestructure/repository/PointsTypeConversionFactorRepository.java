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
                        SELECT pt.*,ptf.id,ptf.costpernight,ot.offertypeid,ot.offertypename FROM points_conversion_factor ptf
                                                join pointstype pt on pt.pointstypeid = ptf.idpointtype
                                                 join offertype ot on ot.offertypeid = ptf.offerttypeid
                                                 order by pt.pointstypeid,ot.offertypeid asc
                        """)
        Flux<PointQuotationRawDto> getAllWithPointType();

}
