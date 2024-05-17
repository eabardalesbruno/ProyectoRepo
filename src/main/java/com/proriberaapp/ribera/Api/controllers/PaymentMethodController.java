package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.services.PaymentMethodBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {
    private final PaymentMethodBookService paymentMethodBookService;

    @Autowired
    public PaymentMethodController(PaymentMethodBookService paymentMethodBookService) {
        this.paymentMethodBookService = paymentMethodBookService;
    }

    @PostMapping
    public Mono<PaymentMethodEntity> createPaymentMethod(@RequestBody PaymentMethodEntity paymentMethod) {
        return paymentMethodBookService.createPaymentMethod(paymentMethod);
    }

    @PutMapping("/{id}")
    public Mono<PaymentMethodEntity> updatePaymentMethod(@PathVariable Integer id, @RequestBody PaymentMethodEntity paymentMethod) {
        return paymentMethodBookService.updatePaymentMethod(id, paymentMethod);
    }

    @GetMapping("/{id}")
    public Mono<PaymentMethodEntity> getPaymentMethodById(@PathVariable Integer id) {
        return paymentMethodBookService.getPaymentMethodById(id);
    }

    @GetMapping
    public Flux<PaymentMethodEntity> getAllPaymentMethods() {
        return paymentMethodBookService.getAllPaymentMethods();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePaymentMethod(@PathVariable Integer id) {
        return paymentMethodBookService.deletePaymentMethod(id);
    }
}
