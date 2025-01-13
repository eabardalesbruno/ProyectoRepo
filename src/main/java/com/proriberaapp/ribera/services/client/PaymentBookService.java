package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.math.BigDecimal;

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
    //Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails(int page, int size);
    Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetails(int page, int size);
    Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetailsPagado(int page, int size);
    Mono<PaymentBookEntity> savePaymentBook(PaymentBookEntity paymentBook);

    Mono<PaymentBookEntity> createPaymentBookAndCalculateCommission(PaymentBookEntity paymentBook, Integer caseType);
}
