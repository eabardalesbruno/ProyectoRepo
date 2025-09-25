package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BeneficiaryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BeneficiaryRepository extends R2dbcRepository<BeneficiaryEntity, Integer> {
    // Nuevos métodos acorde a los campos actuales
    Flux<BeneficiaryEntity> findByNameContainingIgnoreCase(String name);

    Flux<BeneficiaryEntity> findByIdMembership(Integer idMembership);

    Flux<BeneficiaryEntity> findByNameContainingIgnoreCaseAndIdMembership(String name, Integer idMembership);
}
