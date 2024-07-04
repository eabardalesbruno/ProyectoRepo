package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.CustomResponse;
import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/refuse-payments")
public class RefusePaymentController {

    private final RefusePaymentService refusePaymentService;

    @Autowired
    public RefusePaymentController(RefusePaymentService refusePaymentService) {
        this.refusePaymentService = refusePaymentService;
    }

    @GetMapping
    public Flux<RefusePaymentEntity> getAllRefusePayments() {
        return refusePaymentService.getAllRefusePayments();
    }

    @GetMapping("/reason")
    public Flux<RefuseEntity> getRefuseReason() {
        return refusePaymentService.getAllRefuseReason();
    }

    @GetMapping("/{id}")
    public Mono<RefusePaymentEntity> getRefusePaymentById(@PathVariable Integer id) {
        return refusePaymentService.getRefusePaymentById(id);
    }

    @PostMapping
    public Mono<RefusePaymentEntity> createRefusePayment(@RequestBody RefusePaymentEntity refusePayment) {
        return refusePaymentService.saveRefusePayment(refusePayment);
    }

    @PostMapping("/approved")
    public Mono<CustomResponse> updatePendingPay(@RequestBody Map<String, Integer> request) {
        Integer paymentBookId = request.get("paymentBookId");
        if (paymentBookId == null) {
            return Mono.error(new IllegalArgumentException("PaymentBookId is required"));
        }
        return refusePaymentService.updatePendingPayAndSendConfirmation(paymentBookId)
                .then(Mono.just(new CustomResponse("El pago ha sido aprobado exitosamente", request)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRefusePayment(@PathVariable Integer id) {
        return refusePaymentService.deleteRefusePayment(id);
    }
}