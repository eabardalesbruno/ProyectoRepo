package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.RefusePaymentEntity;
import com.proriberaapp.ribera.services.client.RefusePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/refuse-payments")
public class RefusePaymentController {

    private final RefusePaymentService refusePaymentService;

    @Autowired
    public RefusePaymentController(RefusePaymentService refusePaymentService) {
        this.refusePaymentService = refusePaymentService;
    }

    @GetMapping
    public ResponseEntity<Flux<RefusePaymentEntity>> getAllRefusePayments() {
        Flux<RefusePaymentEntity> refusePayments = refusePaymentService.getAllRefusePayments();
        return ResponseEntity.status(HttpStatus.OK).body(refusePayments);
    }

    @PostMapping
    public ResponseEntity<Mono<RefusePaymentEntity>> createRefusePayment(@RequestBody RefusePaymentEntity refusePaymentEntity) {
        Mono<RefusePaymentEntity> createdRefusePayment = refusePaymentService.createRefusePayment(refusePaymentEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRefusePayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mono<RefusePaymentEntity>> updateRefusePayment(@PathVariable("id") Integer id, @RequestBody RefusePaymentEntity refusePaymentEntity) {
        Mono<RefusePaymentEntity> updatedRefusePayment = refusePaymentService.updateRefusePayment(id, refusePaymentEntity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRefusePayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deleteRefusePayment(@PathVariable("id") Integer id) {
        Mono<Void> result = refusePaymentService.deleteRefusePayment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }
}