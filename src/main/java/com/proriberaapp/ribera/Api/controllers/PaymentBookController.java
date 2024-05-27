package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.services.PaymentBookService;
import com.proriberaapp.ribera.services.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment-book")
public class PaymentBookController {
    private final PaymentBookService paymentBookService;
    private final S3Uploader s3Uploader;

    @Autowired
    public PaymentBookController(PaymentBookService paymentBookService, S3Uploader s3Uploader) {
        this.paymentBookService = paymentBookService;
        this.s3Uploader = s3Uploader;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<PaymentBookEntity>> createPaymentBook(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "folderNumber", required = false, defaultValue = "13") Integer folderNumber,
            @ModelAttribute PaymentBookEntity paymentBook) {

        try {
            if (file != null && !file.isEmpty()) {
                String imageUrl = processFileAndGetImageUrl(file, folderNumber);
                paymentBook.setImageVoucher(file); // Asignar directamente el archivo multipart
            }
            return paymentBookService.createPaymentBook(paymentBook)
                    .map(savedPaymentBook -> ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentBook));
        } catch (IOException e) {
            e.printStackTrace();
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    private String processFileAndGetImageUrl(MultipartFile file, int folderNumber) throws IOException {
        return s3Uploader.uploadToS3(file, folderNumber);
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

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        file.transferTo(convertedFile);
        return convertedFile;
    }
}
