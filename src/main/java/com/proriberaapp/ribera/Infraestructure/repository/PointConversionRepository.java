package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Domain.entities.PointTransactionConversionEntity;

public interface PointConversionRepository extends ReactiveCrudRepository<PointTransactionConversionEntity, Integer> {
    
}
