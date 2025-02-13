package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.PointConversionEntity;

@Repository
public interface PointTransferRepository extends ReactiveCrudRepository<PointConversionEntity, Integer> {
    
}
