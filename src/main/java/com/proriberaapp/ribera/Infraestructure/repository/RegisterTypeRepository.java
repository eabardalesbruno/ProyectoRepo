package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RegisterTypeRepository extends R2dbcRepository<RegisterTypeEntity, Integer> {
}
