package com.proriberaapp.ribera.Infraestructure.services.admin.impl;

import com.proriberaapp.ribera.Infraestructure.repository.PaymentStateRepository;
import com.proriberaapp.ribera.Infraestructure.services.admin.PaymentManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentManagerServiceImpl implements PaymentManagerService {
    private final PaymentStateRepository paymentStateRepository;

    @Override
    public Flux<String> listToBeConfirmedPayments() {
        return null;
    }

    @Override
    public Flux<String> listConfirmedPayments() {
        return null;
    }

    @Override
    public Flux<String> listRejectedPayments() {
        return null;
    }

    @Override
    public Flux<String> listPayments() {
        return null;
    }

    @Override
    public Mono<String> confirmPayment(Integer adminId, String paymentId) {
        return null;
    }

    @Override
    public Mono<String> rejectPayment(Integer adminId, String paymentId) {
        return null;
    }

    @Override
    public Mono<String> cancelPayment(Integer adminId, String paymentId) {
        return null;
    }
}
