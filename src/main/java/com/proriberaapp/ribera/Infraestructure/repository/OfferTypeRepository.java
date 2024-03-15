package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OfferTypeRepository extends R2dbcRepository<OfferTypeEntity, Integer> {
}
