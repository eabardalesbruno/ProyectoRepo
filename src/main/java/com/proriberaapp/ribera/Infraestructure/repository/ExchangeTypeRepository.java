package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ExchangeTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeTypeRepository extends R2dbcRepository<ExchangeTypeEntity, Integer> {}