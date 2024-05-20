package com.proriberaapp.ribera.services;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaymentBookService {
    Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook);
    Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook);
    Mono<PaymentBookEntity> getPaymentBookById(Integer id);
    Flux<PaymentBookEntity> getAllPaymentBooks();
    Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId);
    Flux<PaymentBookEntity> getPaymentBooksByClientTypeId(Integer clientTypeId);
    Mono<Void> deletePaymentBook(Integer id);
}
