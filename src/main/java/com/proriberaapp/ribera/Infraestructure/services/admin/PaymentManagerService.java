package com.proriberaapp.ribera.Infraestructure.services.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentManagerService {

    Flux<String> listToBeConfirmedPayments();
    Flux<String> listConfirmedPayments();
    Flux<String> listRejectedPayments();
    Flux<PaymentResponse> listPayments();

    Mono<String> confirmPayment(Integer adminId, String paymentId);
    Mono<String> rejectPayment(Integer adminId, String paymentId);
    Mono<String> cancelPayment(Integer adminId, String paymentId);

}
