package com.proriberaapp.ribera.services.impl;

import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentTokenEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentTokenRepository;
import com.proriberaapp.ribera.services.PaymentBookService;
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
    private final PaymentBookService paymentBookService;
    private final PaymentBookRepository paymentBookRepository;


    @Autowired
    public PaymentTokenServiceImpl(PaymentTokenRepository paymentTokenRepository, PaymentBookService paymentBookService,PaymentBookRepository paymentBookRepository) {
        this.paymentTokenRepository = paymentTokenRepository;
        this.paymentBookService = paymentBookService;
        this.paymentBookRepository = paymentBookRepository;
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
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima")));
                    return now.before(paymentTokenEntity.getEndDate());
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookIfTokenActive(String paymentToken) {
        return paymentTokenRepository.findByPaymentToken(paymentToken)
                .flatMap(paymentTokenEntity -> {
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima")));
                    if (now.before(paymentTokenEntity.getEndDate())) {
                        return paymentBookService.getPaymentBookById(paymentTokenEntity.getPaymentBookId());
                    } else {
                        return Mono.empty();
                    }
                });
    }
    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }
}
