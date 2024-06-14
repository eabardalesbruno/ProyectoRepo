package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.OfferTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.OfferTypeRepository;
import com.proriberaapp.ribera.services.client.OfferTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferTypeServiceImpl implements OfferTypeService {
    private final OfferTypeRepository offerTypeRepository;
    @Override
    public Mono<OfferTypeEntity> save(OfferTypeEntity entity) {
        return offerTypeRepository.findByOfferTypeName(entity).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("OfferType already exists"))
                        : offerTypeRepository.save(entity));
    }

    @Override
    public Flux<OfferTypeEntity> saveAll(List<OfferTypeEntity> entity) {
        return offerTypeRepository.findAllByOfferTypeNameIn(entity)
                .collectList()
                .flatMapMany(offerTypeEntities -> offerTypeRepository.saveAll(
                        entity.stream().filter(offerTypeEntity -> !offerTypeEntities.contains(offerTypeEntity)).toList()
                ));
    }

    @Override
    public Mono<OfferTypeEntity> findById(Integer id) {
        return offerTypeRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("OfferType not found")));
    }

    @Override
    public Flux<OfferTypeEntity> findAll() {
        return offerTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return offerTypeRepository.deleteById(id);
    }

    @Override
    public Mono<OfferTypeEntity> update(OfferTypeEntity entity) {
        return offerTypeRepository.save(entity);
    }
}
