package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentMethodRepository;
import com.proriberaapp.ribera.services.client.PaymentMethodBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentMethodBookServiceImpl implements PaymentMethodBookService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodBookServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public Mono<PaymentMethodEntity> createPaymentMethod(PaymentMethodEntity paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public Mono<PaymentMethodEntity> updatePaymentMethod(Integer id, PaymentMethodEntity paymentMethod) {
        return paymentMethodRepository.findById(id)
                .flatMap(existingMethod -> {
                    existingMethod.setDescription(paymentMethod.getDescription());
                    existingMethod.setState(paymentMethod.getState());
                    return paymentMethodRepository.save(existingMethod);
                });
    }

    @Override
    public Mono<PaymentMethodEntity> getPaymentMethodById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public Flux<PaymentMethodEntity> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public Mono<Void> deletePaymentMethod(Integer id) {
        return paymentMethodRepository.deleteById(id);
    }
}
