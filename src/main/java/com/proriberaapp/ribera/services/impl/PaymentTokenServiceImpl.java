package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentTokenEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentTokenRepository;
import com.proriberaapp.ribera.services.PaymentTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class PaymentTokenServiceImpl implements PaymentTokenService {
    private final PaymentTokenRepository paymentTokenRepository;

    @Autowired
    public PaymentTokenServiceImpl(PaymentTokenRepository paymentTokenRepository) {
        this.paymentTokenRepository = paymentTokenRepository;
    }

    @Override
    public Mono<String> generateAndSaveToken(Integer bookingId, Integer paymentBookId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        return paymentTokenRepository.generateAndSaveToken(token, bookingId, paymentBookId)
                .thenReturn(token);
    }

    @Override
    public Mono<BookingEntity> findBookingByPaymentToken(String paymentToken) {
        return paymentTokenRepository.findBookingByPaymentToken(paymentToken);
    }

    @Override
    public Mono<Integer> findBookingIdByPaymentToken(String paymentToken) {
        return paymentTokenRepository.findBookingIdByPaymentToken(paymentToken);
    }
    @Override
    public Mono<Boolean> isPaymentTokenActive(String paymentToken) {
        return paymentTokenRepository.findByPaymentToken(paymentToken)
                .map(paymentTokenEntity -> {
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
                    return now.before(paymentTokenEntity.getEndDate());
                })
                .defaultIfEmpty(false);
    }
}
