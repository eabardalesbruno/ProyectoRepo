package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.BedroomEntity;
import com.proriberaapp.ribera.Domain.entities.BedsTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BedsTypeRepository;
import com.proriberaapp.ribera.Infraestructure.services.BedsTypeService;
import com.proriberaapp.ribera.Infraestructure.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedsTypeServiceImpl implements BedsTypeService {
    private final BedsTypeRepository bedsTypeRepository;

    @Override
    public Mono<BedsTypeEntity> save(BedsTypeEntity entity) {
        return bedsTypeRepository.save(entity);
    }

    @Override
    public Flux<BedsTypeEntity> saveAll(Flux<BedsTypeEntity> entity) {
        return bedsTypeRepository.saveAll(entity);
    }

    @Override
    public Mono<BedsTypeEntity> findById(Integer id) {
        return bedsTypeRepository.findById(id);
    }

    @Override
    public Flux<BedsTypeEntity> findAll() {
        return bedsTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return bedsTypeRepository.deleteById(id);
    }

    @Override
    public Mono<BedsTypeEntity> update(BedsTypeEntity entity) {
        return bedsTypeRepository.save(entity);
    }
}
