package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentSubtypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentSubtypeRepository;
import com.proriberaapp.ribera.services.PaymentSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentSubtypeServiceImpl implements PaymentSubtypeService {

    private final PaymentSubtypeRepository paymentSubtypeRepository;

    @Autowired
    public PaymentSubtypeServiceImpl(PaymentSubtypeRepository paymentSubtypeRepository) {
        this.paymentSubtypeRepository = paymentSubtypeRepository;
    }

    @Override
    public Mono<PaymentSubtypeEntity> createPaymentSubtype(PaymentSubtypeEntity paymentSubtype) {
        return paymentSubtypeRepository.save(paymentSubtype);
    }

    @Override
    public Mono<PaymentSubtypeEntity> getPaymentSubtype(Integer paymentSubtypeId) {
        return paymentSubtypeRepository.findById(paymentSubtypeId);
    }

    @Override
    public Flux<PaymentSubtypeEntity> getAllPaymentSubtypes() {
        return paymentSubtypeRepository.findAll();
    }

    @Override
    public Mono<Void> deletePaymentSubtype(Integer paymentSubtypeId) {
        return paymentSubtypeRepository.deleteById(paymentSubtypeId);
    }
    @Override
    public Flux<PaymentSubtypeEntity> getPaymentSubtypesByPaymentTypeId(Integer paymentTypeId) {
        return paymentSubtypeRepository.findByPaymentTypeId(paymentTypeId);
    }
}
