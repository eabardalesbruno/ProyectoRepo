package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.Infraestructure.services.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterTypeServiceImpl implements RegisterTypeService {
    @Override
    public Mono<RegisterTypeEntity> save(RegisterTypeEntity registerTypeEntity) {
        return null;
    }

    @Override
    public Flux<RegisterTypeEntity> saveAll(Flux<RegisterTypeEntity> registerTypeEntity) {
        return null;
    }

    @Override
    public Mono<RegisterTypeEntity> findById(String id) {
        return null;
    }

    @Override
    public Flux<RegisterTypeEntity> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<RegisterTypeEntity> update(RegisterTypeEntity registerTypeEntity) {
        return null;
    }
}
