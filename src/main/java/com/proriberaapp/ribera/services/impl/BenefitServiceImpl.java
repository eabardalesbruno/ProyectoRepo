package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.BenefitEntity;
import com.proriberaapp.ribera.Infraestructure.repository.BenefitRepository;
import com.proriberaapp.ribera.services.BenefitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitServiceImpl implements BenefitService {
    private final BenefitRepository benefitRepository;
    @Override
    public Mono<BenefitEntity> save(BenefitEntity entity) {
        return benefitRepository.findByBenefitName(entity.getBenefitName())
                .hasElements()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Benefit already exists"))
                        : benefitRepository.save(entity));
    }

    @Override
    public Flux<BenefitEntity> saveAll(List<BenefitEntity> entity) {
        return benefitRepository.findAllByBenefitNameIn(entity)
                .collectList()
                .flatMapMany(exists -> benefitRepository.saveAll(
                        entity.stream()
                                .filter(e -> !exists.contains(e))
                                .toList()
                ));
    }

    @Override
    public Mono<BenefitEntity> findById(Integer id) {
        return benefitRepository.findById(id)
                .hasElement()
                .flatMap(exists -> exists
                        ? benefitRepository.findById(id)
                        : Mono.error(new IllegalArgumentException("Benefit not found")));
    }

    @Override
    public Flux<BenefitEntity> findAll() {
        return benefitRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return benefitRepository.findById(id)
                .hasElement()
                .flatMap(exists -> exists
                        ? benefitRepository.deleteById(id)
                        : Mono.error(new IllegalArgumentException("Benefit not found")));
    }

    @Override
    public Mono<BenefitEntity> update(BenefitEntity entity) {
        return benefitRepository.findById(entity.getBenefitId())
                .hasElement()
                .flatMap(exists -> exists
                        ? benefitRepository.save(entity)
                        : Mono.error(new IllegalArgumentException("Benefit not found")));
    }
}
