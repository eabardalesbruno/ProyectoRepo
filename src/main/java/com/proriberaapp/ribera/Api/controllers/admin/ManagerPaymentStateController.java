package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Domain.entities.PaymentStateEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/manager/payment-state")
@RequiredArgsConstructor
public class ManagerPaymentStateController {
    private final PaymentStateService paymentStateService;

    @GetMapping("/find/all")
    public Flux<PaymentStateEntity> findAllPaymentStates() {
        return paymentStateService.findAll();
    }

    @GetMapping("/find")
    public Mono<PaymentStateEntity> findPaymentState(
            @RequestParam Integer id
    ) {
        return paymentStateService.findById(id);
    }

    @PostMapping("/register")
    public Mono<PaymentStateEntity> registerPaymentState(
            @RequestBody PaymentStateEntity paymentStateEntity
    ) {
        return paymentStateService.save(paymentStateEntity);
    }

    @PostMapping("/register/all")
    public Flux<PaymentStateEntity> registerAllPaymentStates(
            @RequestBody List<PaymentStateEntity> paymentStateEntity
    ) {
        return paymentStateService.saveAll(paymentStateEntity);
    }

    @PatchMapping("/update")
    public Mono<PaymentStateEntity> updatePaymentState(
            @RequestBody PaymentStateEntity paymentStateEntity
    ) {
        return paymentStateService.update(paymentStateEntity);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deletePaymentState(
            @RequestParam Integer id
    ) {
        return paymentStateService.deleteById(id);
    }
}
