package com.proriberaapp.ribera.Infraestructure.repository;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RegisterTypeRepository extends R2dbcRepository<RegisterTypeEntity, Integer> {
    Mono<RegisterTypeEntity> findByRegisterTypeName(String registerTypeName);
    Flux<RegisterTypeEntity> findByRegisterTypeName(Flux<RegisterTypeEntity> registerTypeName);
}
