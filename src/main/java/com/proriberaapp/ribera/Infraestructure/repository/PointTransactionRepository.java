package com.proriberaapp.ribera.Infraestructure.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.proriberaapp.ribera.Domain.entities.PointTransactionEntity;

@Repository
public interface PointTransactionRepository extends ReactiveCrudRepository<PointTransactionEntity, Integer> {

}