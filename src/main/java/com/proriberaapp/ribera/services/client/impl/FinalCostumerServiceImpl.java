package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.FinalCostumerEntity;
import com.proriberaapp.ribera.Infraestructure.repository.FinalCostumerRepository;
import com.proriberaapp.ribera.services.FinalCostumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinalCostumerServiceImpl implements FinalCostumerService {

    private final FinalCostumerRepository finalCostumerRepository;
    @Override
    public Mono<FinalCostumerEntity> save(FinalCostumerEntity entity) {
        return finalCostumerRepository.findByDocumentNumber(entity).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("FinalCostumer already exists"))
                        : finalCostumerRepository.save(entity));
    }

    @Override
    public Flux<FinalCostumerEntity> saveAll(List<FinalCostumerEntity> entity) {
        return finalCostumerRepository.findAllByDocumentNumberIn(entity)
                .collectList()
                .flatMapMany(finalCostumerEntities -> finalCostumerRepository.saveAll(
                        entity.stream().filter(finalCostumerEntity -> !finalCostumerEntities.contains(finalCostumerEntity)).toList()
                ));
    }

    @Override
    public Mono<FinalCostumerEntity> findById(Integer id) {
        return finalCostumerRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("FinalCostumer not found")));
    }

    @Override
    public Flux<FinalCostumerEntity> findAll() {
        return finalCostumerRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return finalCostumerRepository.deleteById(id);
    }

    @Override
    public Mono<FinalCostumerEntity> update(FinalCostumerEntity entity) {
        return finalCostumerRepository.save(entity);
    }
}
