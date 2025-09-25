package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentStateRepository;
import com.proriberaapp.ribera.services.client.PaymentStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                        : paymentStateRepository.save(paymentStateEntity));
    }

    @Override
    public Flux<PaymentStateEntity> saveAll(List<PaymentStateEntity> paymentStateEntity) {
        return paymentStateRepository.findAllByPaymentStateNameIn(paymentStateEntity)
                .collectList()
                .flatMapMany(paymentStateEntities -> paymentStateRepository.saveAll(
                        paymentStateEntity.stream().filter(
                                paymentStateEntity1 -> !paymentStateEntities.contains(paymentStateEntity1)).toList()
                ));
    }

    @Override
    public Mono<PaymentStateEntity> findById(Integer id) {
        return paymentStateRepository.findById(id);
    }

    @Override
    public Flux<PaymentStateEntity> findAll() {
        return paymentStateRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return paymentStateRepository.deleteById(id);
    }

    @Override
    public Mono<PaymentStateEntity> update(PaymentStateEntity paymentStateEntity) {
        return paymentStateRepository.save(paymentStateEntity);
    }
}
