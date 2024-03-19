package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BedsTypeRepository extends R2dbcRepository<BedsTypeEntity, Integer> {
}
