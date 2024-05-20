package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.PaymentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment-book")
public class PaymentBookController {

    private final PaymentBookService paymentBookService;

    @Autowired
    public PaymentBookController(PaymentBookService paymentBookService) {
        this.paymentBookService = paymentBookService;
    }

    @PostMapping
    public Mono<PaymentBookEntity> createPaymentBook(@RequestBody PaymentBookEntity paymentBook) {
        return paymentBookService.createPaymentBook(paymentBook);
    }

    @PutMapping("/{id}")
    public Mono<PaymentBookEntity> updatePaymentBook(@PathVariable Integer id, @RequestBody PaymentBookEntity paymentBook) {
        return paymentBookService.updatePaymentBook(id, paymentBook);
    }

    @GetMapping("/{id}")
    public Mono<PaymentBookEntity> getPaymentBookById(@PathVariable Integer id) {
        return paymentBookService.getPaymentBookById(id);
    }

    @GetMapping
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookService.getAllPaymentBooks();
    }

    @GetMapping("/user-client/{userClientId}")
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(@PathVariable Integer userClientId) {
        return paymentBookService.getPaymentBooksByUserClientId(userClientId);
    }

    @GetMapping("/client-type/{clientTypeId}")
    public Flux<PaymentBookEntity> getPaymentBooksByClientTypeId(@PathVariable Integer clientTypeId) {
        return paymentBookService.getPaymentBooksByClientTypeId(clientTypeId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePaymentBook(@PathVariable Integer id) {
        return paymentBookService.deletePaymentBook(id);
    }
}
