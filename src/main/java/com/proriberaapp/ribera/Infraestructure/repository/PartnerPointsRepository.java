package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PartnerPointsRepository extends R2dbcRepository<PartnerPointsEntity, Integer> {
}
