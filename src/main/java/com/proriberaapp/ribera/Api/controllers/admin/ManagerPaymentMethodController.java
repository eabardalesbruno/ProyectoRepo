package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/admin/manager/payment-method")
@RequiredArgsConstructor
public class ManagerPaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping("/find/all")
    public Flux<PaymentMethodEntity> findAllPaymentMethods() {
        return paymentMethodService.findAll();
    }

    @GetMapping("/find")
    public Mono<PaymentMethodEntity> findPaymentMethod(
            @RequestParam String id
    ) {
        return paymentMethodService.findById(id);
    }

    @PostMapping("/register")
    public Mono<PaymentMethodEntity> registerPaymentMethod(
            @RequestBody PaymentMethodEntity paymentMethodEntity
    ) {
        return paymentMethodService.save(paymentMethodEntity);
    }

    @PostMapping("/register/all")
    public Flux<PaymentMethodEntity> registerAllPaymentMethods(
            @RequestBody Flux<PaymentMethodEntity> paymentMethodEntity
    ) {
        return paymentMethodService.saveAll(paymentMethodEntity);
    }

    @PatchMapping("/update")
    public Mono<PaymentMethodEntity> updatePaymentMethod(
            @RequestBody PaymentMethodEntity paymentMethodEntity
    ) {
        return paymentMethodService.update(paymentMethodEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deletePaymentMethod(
            @RequestParam String id
    ) {
        return paymentMethodService.deleteById(id);
    }

}
