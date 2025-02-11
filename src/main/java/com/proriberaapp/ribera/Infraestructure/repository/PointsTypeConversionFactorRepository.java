package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.dto.PointQuotationDto;
import com.proriberaapp.ribera.Domain.entities.PointTypeConversionFactorEntity;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsTypeConversionFactorRepository
                extends R2dbcRepository<PointTypeConversionFactorEntity, Integer> {
        @Query("""
                        SELECT pt.*,ptf.id,ptf.factor FROM points_conversion_factor ptf
                                                join pointstype pt on pt.pointstypeid = ptf.idpointtype
                        """)
        Flux<PointQuotationDto> getAllWithPointType();

}
