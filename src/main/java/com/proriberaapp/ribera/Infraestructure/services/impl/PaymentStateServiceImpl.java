package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentStateRepository;
import com.proriberaapp.ribera.Infraestructure.services.PaymentStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStateServiceImpl implements PaymentStateService {
    private final PaymentStateRepository paymentStateRepository;
    @Override
    public Mono<PaymentStateEntity> save(PaymentStateEntity paymentStateEntity) {
        return paymentStateRepository.findByPaymentStateName(paymentStateEntity.getPaymentStateName()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Payment state already exists"))
                        : Mono.just(paymentStateEntity))
                .switchIfEmpty(paymentStateRepository.save(paymentStateEntity));
    }

    @Override
    public Flux<PaymentStateEntity> saveAll(Flux<PaymentStateEntity> paymentStateEntity) {
        return paymentStateRepository.findByPaymentStateName(paymentStateEntity)
                .collectList()
                .flatMapMany(paymentStateEntities -> paymentStateRepository.saveAll(
                        paymentStateEntity.filter(
                                paymentStateEntity1 -> !paymentStateEntities.contains(paymentStateEntity1))
                ));
    }

    @Override
    public Mono<PaymentStateEntity> findById(String id) {
        return paymentStateRepository.findById(Integer.valueOf(id));
    }

    @Override
    public Flux<PaymentStateEntity> findAll() {
        return paymentStateRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return paymentStateRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public Mono<PaymentStateEntity> update(PaymentStateEntity paymentStateEntity) {
        return paymentStateRepository.save(paymentStateEntity);
    }
}
