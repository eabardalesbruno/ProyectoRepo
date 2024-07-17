package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.GenderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface GenderRepository extends R2dbcRepository<GenderEntity, Integer> {
}
