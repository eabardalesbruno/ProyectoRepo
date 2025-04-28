package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.PointConversionRateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PointConversionRateRepository extends ReactiveCrudRepository<PointConversionRateEntity, Integer> {

    Mono<PointConversionRateEntity> findByFamilyId(Integer familyId);

    Mono<Boolean> existsByFamilyId(Integer familyId);

    //aca colocar query
    //Flux<PointConversionRateDto> findPointConversionRateAll();
}
