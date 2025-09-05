package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BeneficiaryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BeneficiaryRepository extends R2dbcRepository<BeneficiaryEntity, Integer> {
    Flux<BeneficiaryEntity> findByNombresContainingIgnoreCase(String nombres);

    Flux<BeneficiaryEntity> findByMembresiaContainingIgnoreCase(String membresia);

    Flux<BeneficiaryEntity> findByNombresContainingIgnoreCaseAndMembresiaContainingIgnoreCase(String nombres,
            String membresia);
}
