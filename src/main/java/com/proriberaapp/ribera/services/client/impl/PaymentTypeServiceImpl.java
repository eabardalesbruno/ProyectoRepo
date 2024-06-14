package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentTypeEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentTypeRepository;
import com.proriberaapp.ribera.services.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    @Autowired
    public PaymentTypeServiceImpl(PaymentTypeRepository paymentTypeRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
    }

    @Override
    public Mono<PaymentTypeEntity> createPaymentType(PaymentTypeEntity paymentType) {
        return paymentTypeRepository.save(paymentType);
    }

    @Override
    public Mono<PaymentTypeEntity> getPaymentType(Integer paymentTypeId) {
        return paymentTypeRepository.findById(paymentTypeId);
    }

    @Override
    public Flux<PaymentTypeEntity> getAllPaymentTypes() {
        return paymentTypeRepository.findAll();
    }

    @Override
    public Mono<Void> deletePaymentType(Integer paymentTypeId) {
        return paymentTypeRepository.deleteById(paymentTypeId);
    }
}