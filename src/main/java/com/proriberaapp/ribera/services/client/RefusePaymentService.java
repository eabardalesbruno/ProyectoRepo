package com.proriberaapp.ribera.services.client;

import java.util.Map;

import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RefusePaymentService {

    Flux<RefusePaymentEntity> getAllRefusePayments();

    Flux<RefuseEntity> getAllRefuseReason();

    Mono<RefusePaymentEntity> getRefusePaymentById(Integer id);

    Mono<RefusePaymentEntity> saveRefusePayment(RefusePaymentEntity refusePayment);

    Mono<Void> deleteRefusePayment(Integer id);

    Mono<Void> updatePendingPayAndSendConfirmation(Integer paymentBookId);

    Mono<Map<String, Object>> getPaymentDetails(Integer paymentBookId);

}
