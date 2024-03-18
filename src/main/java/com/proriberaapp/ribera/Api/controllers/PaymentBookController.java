package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/payment-books")
public class PaymentBookController {

    private final PaymentBookService paymentBookService;

    @Autowired
    public PaymentBookController(PaymentBookService paymentBookService) {
        this.paymentBookService = paymentBookService;
    }

    @GetMapping
    public ResponseEntity<Flux<PaymentBookEntity>> getAllPaymentBooks() {
        Flux<PaymentBookEntity> paymentBooks = paymentBookService.getAllPaymentBooks();
        return ResponseEntity.status(HttpStatus.OK).body(paymentBooks);
    }
}