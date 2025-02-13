package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proriberaapp.ribera.Domain.entities.PointConversionEntity;
import com.proriberaapp.ribera.Domain.entities.PointTransactionTypeEntity;

import reactor.core.publisher.Mono;

public interface PointTransactionTypeRepository extends ReactiveCrudRepository<PointTransactionTypeEntity, Integer> {
    @Query("SELECT * FROM pointtransactiontype WHERE name = :name")
    Mono<PointTransactionTypeEntity> findByName(String name);

}
