package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.TransactionCategoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionCategoryRepository extends R2dbcRepository<TransactionCategoryEntity,Integer> {
}
