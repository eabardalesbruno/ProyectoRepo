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
        return partnerPointsRepository.findByPartnerIdAndUserId(partnerPointsEntity.getPartnerPointId(), partnerPointsEntity.getUserId()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Partner points already exists"))
                        : Mono.just(partnerPointsEntity))
                .switchIfEmpty(partnerPointsRepository.save(partnerPointsEntity));
    }

    @Override
    public Flux<PartnerPointsEntity> saveAll(Flux<PartnerPointsEntity> partnerPointsEntity) {
        Flux<Integer> partnerPointsIds = partnerPointsEntity.map(PartnerPointsEntity::getPartnerPointId);
        Flux<Integer> userIds = partnerPointsEntity.map(PartnerPointsEntity::getUserId);
        return partnerPointsRepository.findByPartnerIdAndUserId(partnerPointsIds, userIds)
                .collectList()
                .flatMapMany(partnerPointsEntities -> partnerPointsRepository.saveAll(
                        partnerPointsEntity.filter(
                                partnerPointsEntity1 -> !partnerPointsEntities.contains(partnerPointsEntity1))
                ));
    }

    @Override
    public Mono<PartnerPointsEntity> findById(String id) {
        return partnerPointsRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<PartnerPointsEntity> findAll() {
        return partnerPointsRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return partnerPointsRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<PartnerPointsEntity> update(PartnerPointsEntity partnerPointsEntity) {
        return partnerPointsRepository.save(partnerPointsEntity);
    }
}
