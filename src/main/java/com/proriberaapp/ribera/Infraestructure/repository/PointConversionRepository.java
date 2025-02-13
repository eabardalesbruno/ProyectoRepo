package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Domain.entities.PointConversionEntity;

public interface PointConversionRepository extends ReactiveCrudRepository<PointConversionEntity, Integer> {
    
}
