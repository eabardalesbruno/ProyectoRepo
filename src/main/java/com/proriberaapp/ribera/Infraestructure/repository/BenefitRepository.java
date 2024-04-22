package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.BenefitEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BenefitRepository extends R2dbcRepository<BenefitEntity, Integer>{
    Flux<BenefitEntity> findByBenefitName(String benefitName);

    Flux<BenefitEntity> findAllByBenefitNameIn(List<BenefitEntity> entity);
}
