package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.ServicesEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicesService {
    Mono<ServicesEntity> createService(ServicesEntity service);
    Mono<ServicesEntity> getServiceById(Integer id);
    Flux<ServicesEntity> getAllServices();
    Mono<ServicesEntity> updateService(Integer id, ServicesEntity service);
    Mono<Void> deleteService(Integer id);
}
