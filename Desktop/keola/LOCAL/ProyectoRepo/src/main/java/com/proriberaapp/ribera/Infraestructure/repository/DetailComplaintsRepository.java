package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.DetailComplaintsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailComplaintsRepository extends R2dbcRepository<DetailComplaintsEntity, Integer> {
}
