package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentMethodRepository;
import com.proriberaapp.ribera.Infraestructure.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    @Override
    public Mono<PaymentMethodEntity> save(PaymentMethodEntity paymentMethodEntity) {
        return paymentMethodRepository.findByDescription(paymentMethodEntity.getDescription()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Payment method already exists"))
                        : Mono.just(paymentMethodEntity))
                .switchIfEmpty(paymentMethodRepository.save(paymentMethodEntity));
    }

    @Override
    public Flux<PaymentMethodEntity> saveAll(Flux<PaymentMethodEntity> paymentMethodEntity) {
        return paymentMethodRepository.findByDescription(paymentMethodEntity)
                .collectList()
                .flatMapMany(paymentMethodEntities -> paymentMethodRepository.saveAll(
                        paymentMethodEntity.filter(
                                paymentMethodEntity1 -> !paymentMethodEntities.contains(paymentMethodEntity1))
                ));
    }

    @Override
    public Mono<PaymentMethodEntity> findById(String id) {
        return paymentMethodRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<PaymentMethodEntity> findAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return paymentMethodRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<PaymentMethodEntity> update(PaymentMethodEntity paymentMethodEntity) {
        return paymentMethodRepository.save(paymentMethodEntity);
    }
}
