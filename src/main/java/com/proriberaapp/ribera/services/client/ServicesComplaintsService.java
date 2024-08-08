package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.ServicesComplaintsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicesComplaintsService {
    Mono<ServicesComplaintsEntity> create(ServicesComplaintsEntity entity);
    Mono<ServicesComplaintsEntity> update(ServicesComplaintsEntity entity);
    Mono<Void> deleteById(Integer id);
    Mono<ServicesComplaintsEntity> findById(Integer id);
    Flux<ServicesComplaintsEntity> findAll();
}
