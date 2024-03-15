package com.proriberaapp.ribera.Infraestructure.services.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
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
    public Mono<PaymentMethodEntity> save(PaymentMethodRequest paymentMethodRequest) {
        return paymentMethodRepository.findByDescription(paymentMethodRequest.description()).hasElement()
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalArgumentException("Payment method already exists"))
                        : paymentMethodRepository.save(paymentMethodRequest.toEntity()));
    }

    @Override
    public Flux<PaymentMethodEntity> saveAll(Flux<PaymentMethodRequest> paymentMethodRequest) {
        return paymentMethodRepository.findByDescription(paymentMethodRequest)
                .collectList()
                .flatMapMany(paymentMethodEntities -> paymentMethodRepository.saveAll(
                        paymentMethodRequest.filter(
                                paymentMethodEntity1 -> !paymentMethodEntities.contains(paymentMethodEntity1.toEntity()))
                                .map(PaymentMethodRequest::toEntity)
                ));
    }
    @Override
    public Mono<PaymentMethodEntity> findById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public Flux<PaymentMethodEntity> findAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return paymentMethodRepository.deleteById(id);
    }

    @Override
    public Mono<PaymentMethodEntity> update(PaymentMethodRequest paymentMethodRequest) {
        PaymentMethodEntity paymentMethodEntity = paymentMethodRequest.toEntity();
        return paymentMethodRepository.save(paymentMethodEntity);
    }
}
