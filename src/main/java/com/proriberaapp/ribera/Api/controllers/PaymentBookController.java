package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping
    public ResponseEntity<Mono<PaymentBookEntity>> createPaymentBook(@RequestBody PaymentBookEntity paymentBookEntity) {
        Mono<PaymentBookEntity> createdPaymentBook = paymentBookService.createPaymentBook(paymentBookEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<PaymentBookEntity>> updatePaymentBook(@PathVariable("id") Integer id, @RequestBody PaymentBookEntity paymentBookEntity) {
        Mono<PaymentBookEntity> updatedPaymentBook = paymentBookService.updatePaymentBook(id, paymentBookEntity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPaymentBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deletePaymentBook(@PathVariable("id") Integer id) {
        Mono<Void> result = paymentBookService.deletePaymentBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }
}