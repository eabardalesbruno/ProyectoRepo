package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentSubtypeEntity;
import com.proriberaapp.ribera.services.PaymentSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/paymentsubtypes")
public class PaymentSubtypeController {
    private final PaymentSubtypeService paymentSubtypeService;

    @Autowired
    public PaymentSubtypeController(PaymentSubtypeService paymentSubtypeService) {
        this.paymentSubtypeService = paymentSubtypeService;
    }

    @PostMapping
    public Mono<PaymentSubtypeEntity> createPaymentSubtype(@RequestBody PaymentSubtypeEntity paymentSubtype) {
        return paymentSubtypeService.createPaymentSubtype(paymentSubtype);
    }

    @GetMapping("/{paymentSubtypeId}")
    public Mono<PaymentSubtypeEntity> getPaymentSubtype(@PathVariable Integer paymentSubtypeId) {
        return paymentSubtypeService.getPaymentSubtype(paymentSubtypeId);
    }

    @GetMapping
    public Flux<PaymentSubtypeEntity> getAllPaymentSubtypes() {
        return paymentSubtypeService.getAllPaymentSubtypes();
    }

    @DeleteMapping("/{paymentSubtypeId}")
    public Mono<Void> deletePaymentSubtype(@PathVariable Integer paymentSubtypeId) {
        return paymentSubtypeService.deletePaymentSubtype(paymentSubtypeId);
    }
    @GetMapping("/bypaymenttype/{paymentTypeId}")
    public Flux<PaymentSubtypeEntity> getPaymentSubtypesByPaymentTypeId(@PathVariable Integer paymentTypeId) {
        return paymentSubtypeService.getPaymentSubtypesByPaymentTypeId(paymentTypeId);
    }
}