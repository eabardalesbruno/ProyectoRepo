package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RegisterTypeService {
    Mono<RegisterTypeEntity> save(RegisterTypeEntity registerTypeEntity);
    Flux<RegisterTypeEntity> saveAll(Flux<RegisterTypeEntity> registerTypeEntity);
    Mono<RegisterTypeEntity> findById(String id);
    Flux<RegisterTypeEntity> findAll();
    Mono<Void> deleteById(String id);
    Mono<RegisterTypeEntity> update(RegisterTypeEntity registerTypeEntity);

}
