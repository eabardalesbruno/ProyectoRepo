package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

public interface PaymentBookService {

    Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity paymentBook);

    Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook);

    Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook);

    Mono<PaymentBookEntity> getPaymentBookById(Integer id);

    Flux<PaymentBookEntity> getAllPaymentBooks();

    Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId);

    Mono<Void> deletePaymentBook(Integer id);
    Mono<Void> updateBookingStateIfRequired(Integer bookingId);
    Mono<PaymentBookEntity> findById(Integer id);
    Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails();

}
