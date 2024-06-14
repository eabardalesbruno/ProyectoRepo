package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.RegisterTypeRequest;
import com.proriberaapp.ribera.Domain.entities.RegisterTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.RegisterTypeRepository;
import com.proriberaapp.ribera.services.client.RegisterTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterTypeServiceImpl implements RegisterTypeService {
    private final RegisterTypeRepository registerTypeRepository;
    @Override
    public Mono<RegisterTypeEntity> save(RegisterTypeRequest registerTypeRequest) {
        return registerTypeRepository.findByRegisterTypeName(registerTypeRequest.registerTypeName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Register type already exists"))
                        : registerTypeRepository.save(registerTypeRequest.toEntity()));
    }

    @Override
    public Flux<RegisterTypeEntity> saveAll(List<RegisterTypeRequest> registerTypeRequest) {
        List<RegisterTypeEntity> registerTypeEntity = RegisterTypeRequest.toEntity(registerTypeRequest);
        return registerTypeRepository.findAllByRegisterTypeNameIn(registerTypeEntity)
                .collectList()
                .flatMapMany(registerTypeEntities -> registerTypeRepository.saveAll(
                        registerTypeEntity.stream().filter(
                                registerTypeEntity1 -> !registerTypeEntities.contains(registerTypeEntity1)).toList()
                ));
    }

    @Override
    public Mono<RegisterTypeEntity> findById(Integer id) {
        return registerTypeRepository.findById(id);
    }

    @Override
    public Flux<RegisterTypeEntity> findAll() {
        return registerTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return registerTypeRepository.deleteById(id);
    }

    @Override
    public Mono<RegisterTypeEntity> update(RegisterTypeRequest registerTypeRequest) {
        RegisterTypeEntity registerTypeEntity = registerTypeRequest.toEntity();
        return registerTypeRepository.save(registerTypeEntity);
    }
}
