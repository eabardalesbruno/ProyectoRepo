package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PartnerPointsEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PartnerPointsRepository;
import com.proriberaapp.ribera.services.client.PartnerPointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

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
    public Flux<PartnerPointsEntity> saveAll(List<PartnerPointsEntity> entity) {
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

    @Override
    public Mono<PartnerPointsEntity> incrementPoints(PartnerPointsEntity partnerPointsEntity, Integer increment) {
        return partnerPointsRepository.findByUserClientId(partnerPointsEntity.getUserClientId())
                .switchIfEmpty(partnerPointsRepository.save(partnerPointsEntity))
                .map(partnerPointsEntity1 -> {
                    partnerPointsEntity1.setPoints(partnerPointsEntity1.getPoints() + increment);
                    return partnerPointsEntity1;
                })
                .flatMap(partnerPointsRepository::save);
    }

    @Override
    public Mono<PartnerPointsEntity> decrementPoints(PartnerPointsEntity partnerPointsEntity, Integer decrement) {
        return partnerPointsRepository.findByUserClientId(partnerPointsEntity.getUserClientId())
                .switchIfEmpty(partnerPointsRepository.save(partnerPointsEntity))
                .map(partnerPointsEntity1 -> {
                    partnerPointsEntity1.setPoints(partnerPointsEntity1.getPoints() - decrement);
                    return partnerPointsEntity1;
                })
                .flatMap(partner -> partner.getPoints() < 0 ? Mono.error(new IllegalArgumentException("Points cannot be negative")) : partnerPointsRepository.save(partner));
    }

    @Override
    public Mono<PartnerPointsEntity> paymentPoints(Integer userClientId, Integer pointsBuy, BigDecimal amountFinal) {
        return null;
    }

    @Override
    public Mono<PartnerPointsEntity> getPartnerPointsByUserClientId(Integer userClientId) {
        return partnerPointsRepository.findByUserClientId(userClientId);
    }
}
