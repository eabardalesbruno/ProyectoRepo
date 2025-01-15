package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.CancelEntity;
import com.proriberaapp.ribera.Domain.entities.CancelPaymentEntity;
import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CancelPaymentService {
    Flux<CancelPaymentEntity> getAllCancelPayments();
    Flux<CancelEntity> getAllCancelReason();
    Mono<CancelPaymentEntity> getCancelPaymentById(Integer id);
    Mono<Void> saveCancelPayment(CancelPaymentEntity cancelPayment);
    Mono<Void> deleteCancelPayment(Integer id);
    Mono<Void> updatePendingPayAndSendConfirmation(Integer paymentBookId);

}
