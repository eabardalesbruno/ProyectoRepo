package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PartnerPointsRepository;
import com.proriberaapp.ribera.Infraestructure.services.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerPointsServiceImpl implements PartnerPointsService {
    private final PartnerPointsRepository partnerPointsRepository;
    @Override
    public Mono<PartnerPointsEntity> save(PartnerPointsEntity partnerPointsEntity) {
        return partnerPointsRepository.findByPartnerPointIdAndUserClientId(partnerPointsEntity.getPartnerPointId(), partnerPointsEntity.getUserClientId()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Partner points already exists"))
                        : partnerPointsRepository.save(partnerPointsEntity));
    }

    @Override
    public Flux<PartnerPointsEntity> saveAll(Flux<PartnerPointsEntity> entity) {
        return null;
    }

    @Override
    public Mono<PartnerPointsEntity> findById(Integer id) {
        return partnerPointsRepository.findById(id);
    }

    @Override
    public Flux<PartnerPointsEntity> findAll() {
        return partnerPointsRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return partnerPointsRepository.deleteById(id);
    }

    @Override
    public Mono<PartnerPointsEntity> update(PartnerPointsEntity partnerPointsEntity) {
        return partnerPointsRepository.save(partnerPointsEntity);
    }
}
