package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.dto.PaymentTokenResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.PaymentBookService;
import com.proriberaapp.ribera.services.PaymentTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment-token")
public class PaymentTokenController {
    private final PaymentTokenService paymentTokenService;
    private final PaymentBookService paymentBookService;

    @Autowired
    public PaymentTokenController(PaymentTokenService paymentTokenService, PaymentBookService paymentBookService) {
        this.paymentTokenService = paymentTokenService;
        this.paymentBookService = paymentBookService;
    }
//jose
    @PostMapping("/{bookingId}/{paymentBookId}")
    public Mono<ResponseEntity<PaymentTokenResponse>> generateAndSaveToken(
            @PathVariable Integer bookingId,
            @PathVariable Integer paymentBookId) {
        return paymentTokenService.generateAndSaveToken(bookingId, paymentBookId)
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(new PaymentTokenResponse(token)));
    }

    @GetMapping("/{paymentToken}")
    public Mono<ResponseEntity<BookingEntity>> getBookingByPaymentToken(@PathVariable String paymentToken) {
        return paymentTokenService.findBookingByPaymentToken(paymentToken)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{paymentToken}/bookingid")
    public Mono<Integer> getBookingIdByPaymentToken(@PathVariable("paymentToken") String paymentToken) {
        return paymentTokenService.findBookingIdByPaymentToken(paymentToken);
    }

    @GetMapping("/{paymentToken}/active")
    public Mono<ResponseEntity<Map<String, Boolean>>> isPaymentTokenActive(@PathVariable String paymentToken) {
        return paymentTokenService.isPaymentTokenActive(paymentToken)
                .map(isActive -> {
                    Map<String, Boolean> response = new HashMap<>();
                    response.put("active", isActive);
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/{paymentToken}/paymentbook")
    public Mono<ResponseEntity<Map<String, Object>>> getPaymentBookIfTokenActive(@PathVariable String paymentToken) {
        return paymentTokenService.getPaymentBookIfTokenActiveWithDetails(paymentToken)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
