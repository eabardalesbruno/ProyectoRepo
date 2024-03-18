package com.proriberaapp.ribera.Infraestructure.services;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}