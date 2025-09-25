package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.ServicesComplaintsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ServicesComplaintsRepository;
import com.proriberaapp.ribera.services.client.ServicesComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServicesComplaintsServiceImpl implements ServicesComplaintsService {

    private final ServicesComplaintsRepository servicesComplaintsRepository;

    @Autowired
    public ServicesComplaintsServiceImpl(ServicesComplaintsRepository servicesComplaintsRepository) {
        this.servicesComplaintsRepository = servicesComplaintsRepository;
    }

    @Override
    public Mono<ServicesComplaintsEntity> create(ServicesComplaintsEntity entity) {
        return servicesComplaintsRepository.save(entity);
    }

    @Override
    public Mono<ServicesComplaintsEntity> update(ServicesComplaintsEntity entity) {
        return servicesComplaintsRepository.save(entity);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return servicesComplaintsRepository.deleteById(id);
    }

    @Override
    public Mono<ServicesComplaintsEntity> findById(Integer id) {
        return servicesComplaintsRepository.findById(id);
    }

    @Override
    public Flux<ServicesComplaintsEntity> findAll() {
        return servicesComplaintsRepository.findAll();
    }
}
