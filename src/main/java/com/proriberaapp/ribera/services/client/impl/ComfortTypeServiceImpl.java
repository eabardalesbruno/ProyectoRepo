package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.ComfortTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.ComfortTypeRepository;
import com.proriberaapp.ribera.services.client.ComfortTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComfortTypeServiceImpl implements ComfortTypeService {
    private final ComfortTypeRepository comfortTypeRepository;
    @Override
    public Mono<ComfortTypeEntity> save(ComfortTypeEntity entity) {
        entity.setActive(true);
        return comfortTypeRepository.findByComfortTypeName(entity).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("ComfortType already exists"))
                        : comfortTypeRepository.save(entity));
    }

    @Override
    public Flux<ComfortTypeEntity> saveAll(List<ComfortTypeEntity> entity) {
        return comfortTypeRepository.findAllByComfortTypeNameIn(entity)
                .collectList()
                .flatMapMany(comfortTypeEntities -> comfortTypeRepository.saveAll(
                        entity.stream()
                                .peek(comfortTypeEntity -> comfortTypeEntity.setActive(true))
                                .filter(comfortTypeEntity -> !comfortTypeEntities.contains(comfortTypeEntity)).toList()
                ));
    }

    @Override
    public Mono<ComfortTypeEntity> findById(Integer id) {
        return comfortTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("ComfortType not found")));
    }

    @Override
    public Flux<ComfortTypeEntity> findAll() {
        return comfortTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return comfortTypeRepository.deleteById(id);
    }

    @Override
    public Mono<ComfortTypeEntity> update(ComfortTypeEntity entity) {
        return comfortTypeRepository.save(entity);
    }
}
