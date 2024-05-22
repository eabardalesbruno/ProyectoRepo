package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentTypeEntity;
import com.proriberaapp.ribera.services.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/paymenttypes")
public class PaymentTypeController {
    private final PaymentTypeService paymentTypeService;

    @Autowired
    public PaymentTypeController(PaymentTypeService paymentTypeService) {
        this.paymentTypeService = paymentTypeService;
    }

    @PostMapping
    public Mono<PaymentTypeEntity> createPaymentType(@RequestBody PaymentTypeEntity paymentType) {
        return paymentTypeService.createPaymentType(paymentType);
    }

    @GetMapping("/{paymentTypeId}")
    public Mono<PaymentTypeEntity> getPaymentType(@PathVariable Integer paymentTypeId) {
        return paymentTypeService.getPaymentType(paymentTypeId);
    }

    @GetMapping
    public Flux<PaymentTypeEntity> getAllPaymentTypes() {
        return paymentTypeService.getAllPaymentTypes();
    }

    @DeleteMapping("/{paymentTypeId}")
    public Mono<Void> deletePaymentType(@PathVariable Integer paymentTypeId) {
        return paymentTypeService.deletePaymentType(paymentTypeId);
    }
}