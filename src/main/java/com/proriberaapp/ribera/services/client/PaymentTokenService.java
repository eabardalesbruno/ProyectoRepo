package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentTokenEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface PaymentTokenService {

    Mono<String> generateAndSaveToken(Integer bookingId, Integer paymentBookId);
    Mono<BookingEntity> findBookingByPaymentToken(String paymentToken);
    Mono<Integer> findBookingIdByPaymentToken(String paymentToken);
    Mono<Boolean> isPaymentTokenActive(String paymentToken);

    Mono<PaymentBookEntity> getPaymentBookIfTokenActive(String paymentToken);
    Mono<PaymentBookEntity> findById(Integer id);
    Mono<Map<String, Object>> getPaymentBookIfTokenActiveWithDetails(String paymentToken);

}