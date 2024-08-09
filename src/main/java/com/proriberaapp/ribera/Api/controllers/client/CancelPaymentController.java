package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Api.controllers.client.dto.CustomResponse;
import com.proriberaapp.ribera.Domain.entities.CancelEntity;
import com.proriberaapp.ribera.Domain.entities.CancelPaymentEntity;
import com.proriberaapp.ribera.Domain.entities.RefuseEntity;
import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.services.client.CancelPaymentService;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cancel-payments")
public class CancelPaymentController {

    private final CancelPaymentService cancelPaymentService;

    @Autowired
    public CancelPaymentController(CancelPaymentService cancelPaymentService) {
        this.cancelPaymentService = cancelPaymentService;
    }

    @GetMapping
    public Flux<CancelPaymentEntity> getAllCancelPayments() {
        return cancelPaymentService.getAllCancelPayments();
    }

    @GetMapping("/reason")
    public Flux<CancelEntity> getCancelReason() {
        return cancelPaymentService.getAllCancelReason();
    }

    @GetMapping("/{id}")
    public Mono<CancelPaymentEntity> getCancelPaymentById(@PathVariable Integer id) {
        return cancelPaymentService.getCancelPaymentById(id);
    }

    @PostMapping
    public Mono<CancelPaymentEntity> createCancelPayment(@RequestBody CancelPaymentEntity cancelPayment) {
        return cancelPaymentService.saveCancelPayment(cancelPayment);
    }

    @PostMapping("/approved")
    public Mono<CustomResponse> updatePendingPay(@RequestBody Map<String, Integer> request) {
        Integer paymentBookId = request.get("paymentBookId");
        if (paymentBookId == null) {
            return Mono.error(new IllegalArgumentException("El ID de pago es requerido"));
        }
        return cancelPaymentService.updatePendingPayAndSendConfirmation(paymentBookId)
                .then(Mono.just(new CustomResponse("El pago ha sido aprobado exitosamente", request)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCancelPayment(@PathVariable Integer id) {
        return cancelPaymentService.deleteCancelPayment(id);
    }
}
