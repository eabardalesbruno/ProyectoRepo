package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentMethodRepository;
import com.proriberaapp.ribera.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Flux<PaymentMethodEntity> saveAll(List<PaymentMethodRequest> paymentMethodRequest) {
        return paymentMethodRepository.findAllByDescriptionIn(paymentMethodRequest)
                .collectList()
                .flatMapMany(paymentMethodEntities -> paymentMethodRepository.saveAll(
                        paymentMethodRequest.stream().map(PaymentMethodRequest::toEntity)
                                .filter(
                                        entity -> !paymentMethodEntities.contains(entity)).toList()
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
