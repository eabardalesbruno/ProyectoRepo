package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.ServicesComplaintsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ServicesComplaintsRepository extends R2dbcRepository<ServicesComplaintsEntity, Integer> {
}
