package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PaymentBookService {

    private final PaymentBookRepository paymentBookRepository;

    @Autowired
    public PaymentBookService(PaymentBookRepository paymentBookRepository) {
        this.paymentBookRepository = paymentBookRepository;
    }

    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBookEntity) {
        return paymentBookRepository.save(paymentBookEntity);
    }

    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBookEntity) {
        return paymentBookRepository.findById(id)
                .flatMap(existingPaymentBook -> {
                    paymentBookEntity.setPaymentBookId(id);
                    return paymentBookRepository.save(paymentBookEntity);
                });
    }

    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }
}