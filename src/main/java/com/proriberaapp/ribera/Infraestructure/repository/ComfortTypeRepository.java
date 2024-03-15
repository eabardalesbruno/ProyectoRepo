package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ComfortTypeRepository extends R2dbcRepository<ComfortTypeEntity, Integer> {
}
