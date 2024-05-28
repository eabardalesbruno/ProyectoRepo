package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.PaymentBookService;
import com.proriberaapp.ribera.services.S3Uploader;
import com.proriberaapp.ribera.services.admin.impl.S3ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

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

    @GetMapping("/costfinal/{bookingId}")
    public Mono<BigDecimal> getCostFinalByBookingId(@PathVariable Integer bookingId) {
        return paymentBookService.getCostFinalByBookingId(bookingId);
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

}
