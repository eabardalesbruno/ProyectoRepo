package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.TermsVersionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface TermsVersionRepository extends R2dbcRepository<TermsVersionEntity, Integer> {
}
