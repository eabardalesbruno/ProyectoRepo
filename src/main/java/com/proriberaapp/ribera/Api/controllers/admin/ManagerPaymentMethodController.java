package com.proriberaapp.ribera.Api.controllers.admin;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentMethodRequest;
import com.proriberaapp.ribera.Domain.entities.PaymentMethodEntity;
import com.proriberaapp.ribera.Infraestructure.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
            @RequestParam Integer id
    ) {
        return paymentMethodService.findById(id);
    }

    @PostMapping("/register")
    public Mono<PaymentMethodEntity> registerPaymentMethod(
            @RequestBody PaymentMethodRequest paymentMethodEntity
    ) {
        return paymentMethodService.save(paymentMethodEntity);
    }

    @PostMapping("/register/all")
    public Flux<PaymentMethodEntity> registerAllPaymentMethods(
            @RequestBody List<PaymentMethodRequest> paymentMethodEntity
    ) {
        return paymentMethodService.saveAll(paymentMethodEntity);
    }

    @PatchMapping("/update")
    public Mono<PaymentMethodEntity> updatePaymentMethod(
            @RequestBody PaymentMethodRequest paymentMethodRequest
    ) {
        return paymentMethodService.update(paymentMethodRequest);
    }

    @DeleteMapping("/delete")
    public Mono<Void> deletePaymentMethod(
            @RequestParam Integer id
    ) {
        return paymentMethodService.deleteById(id);
    }

}
