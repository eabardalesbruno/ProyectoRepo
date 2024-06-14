package com.proriberaapp.ribera.Api.controllers.client;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.client.PaymentBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-book")
public class PaymentBookController {
    private final PaymentBookService paymentBookService;

    @Autowired
    public PaymentBookController(PaymentBookService paymentBookService) {
        this.paymentBookService = paymentBookService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<PaymentBookEntity>> createPaymentBook(
            @RequestBody PaymentBookEntity paymentBook) {

        return paymentBookService.createPaymentBook(paymentBook)
                .map(savedPaymentBook -> ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentBook))
                .onErrorResume(error -> Mono.error(new RuntimeException("Error al cargar la imagen al servidor S3."))) ;
    }

    @PostMapping("/booking-pay")
    public Mono<ResponseEntity<PaymentBookEntity>> createPaymentBookPay(@RequestBody PaymentBookEntity paymentBook) {
        return paymentBookService.createPaymentBookPay(paymentBook)
                .map(savedPaymentBook -> ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentBook));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PaymentBookEntity>> updatePaymentBook(@PathVariable Integer id,
                                                                     @RequestBody PaymentBookEntity paymentBook) {
        return paymentBookService.updatePaymentBook(id, paymentBook)
                .map(updatedPaymentBook -> ResponseEntity.ok().body(updatedPaymentBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentBookEntity>> getPaymentBookById(@PathVariable Integer id) {
        return paymentBookService.getPaymentBookById(id)
                .map(paymentBook -> ResponseEntity.ok().body(paymentBook))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookService.getAllPaymentBooks();
    }

    @GetMapping("/user-client/{userClientId}")
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(@PathVariable Integer userClientId) {
        return paymentBookService.getPaymentBooksByUserClientId(userClientId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletePaymentBook(@PathVariable Integer id) {
        return paymentBookService.deletePaymentBook(id);
    }

    private String generateUniquePaymentToken() {
        // Implementa la lógica para generar un token único (por ejemplo, utilizando JWT)
        return UUID.randomUUID().toString();
    }
}
