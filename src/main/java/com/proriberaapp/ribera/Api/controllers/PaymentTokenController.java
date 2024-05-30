package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Api.controllers.dto.PaymentTokenResponse;
import com.proriberaapp.ribera.Domain.entities.BookingEntity;
import com.proriberaapp.ribera.services.PaymentTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payment-token")
public class PaymentTokenController {
    private final PaymentTokenService paymentTokenService;

    @Autowired
    public PaymentTokenController(PaymentTokenService paymentTokenService) {
        this.paymentTokenService = paymentTokenService;
    }

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
}
