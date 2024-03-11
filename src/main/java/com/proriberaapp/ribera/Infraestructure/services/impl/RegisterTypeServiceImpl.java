package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RegisterTypeRepository;
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
    private final RegisterTypeRepository registerTypeRepository;
    @Override
    public Mono<RegisterTypeEntity> save(RegisterTypeEntity registerTypeEntity) {
        return registerTypeRepository.findByRegisterTypeName(registerTypeEntity.getRegisterTypeName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Register type already exists"))
                        : Mono.just(registerTypeEntity))
                .switchIfEmpty(registerTypeRepository.save(registerTypeEntity));
    }

    @Override
    public Flux<RegisterTypeEntity> saveAll(Flux<RegisterTypeEntity> registerTypeEntity) {
        return registerTypeRepository.findByRegisterTypeName(registerTypeEntity)
                .collectList()
                .flatMapMany(registerTypeEntities -> registerTypeRepository.saveAll(
                        registerTypeEntity.filter(
                                registerTypeEntity1 -> !registerTypeEntities.contains(registerTypeEntity1))
                ));
    }

    @Override
    public Mono<RegisterTypeEntity> findById(String id) {
        return registerTypeRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<RegisterTypeEntity> findAll() {
        return registerTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return registerTypeRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<RegisterTypeEntity> update(RegisterTypeEntity registerTypeEntity) {
        return registerTypeRepository.save(registerTypeEntity);
    }
}
