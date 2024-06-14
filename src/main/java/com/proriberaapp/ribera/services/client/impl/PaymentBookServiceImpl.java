package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.PaymentBookService;
import com.proriberaapp.ribera.services.client.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class PaymentBookServiceImpl implements PaymentBookService {
    private final PaymentBookRepository paymentBookRepository;
    private final BookingService bookingService;

    private final S3Uploader s3Uploader;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,BookingService bookingService, S3Uploader s3Uploader) {
        this.paymentBookRepository = paymentBookRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBook(PaymentBookEntity paymentBook) {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        paymentBook.setPaymentDate(timestamp);

        return paymentBookRepository.save(paymentBook);
    }

    @Override
    public Mono<PaymentBookEntity> createPaymentBookPay(PaymentBookEntity paymentBook) {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("America/Lima"));
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        paymentBook.setPaymentDate(timestamp);
        return paymentBookRepository.save(paymentBook)
                .flatMap(savedPaymentBook -> updateBookingStateIfRequired(savedPaymentBook.getBookingId()).thenReturn(savedPaymentBook));
    }

    @Override
    public Mono<PaymentBookEntity> updatePaymentBook(Integer id, PaymentBookEntity paymentBook) {
        return paymentBookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setBookingId(paymentBook.getBookingId());
                    existingBook.setUserClientId(paymentBook.getUserClientId());
                    existingBook.setRefuseReasonId(paymentBook.getRefuseReasonId());
                    existingBook.setPaymentMethodId(paymentBook.getPaymentMethodId());
                    existingBook.setPaymentStateId(paymentBook.getPaymentStateId());
                    existingBook.setPaymentTypeId(paymentBook.getPaymentTypeId());
                    existingBook.setPaymentSubTypeId(paymentBook.getPaymentSubTypeId());
                    existingBook.setCurrencyTypeId(paymentBook.getCurrencyTypeId());
                    existingBook.setAmount(paymentBook.getAmount());
                    existingBook.setDescription(paymentBook.getDescription());
                    existingBook.setPaymentDate(paymentBook.getPaymentDate());
                    existingBook.setOperationCode(paymentBook.getOperationCode());
                    existingBook.setNote(paymentBook.getNote());
                    existingBook.setTotalCost(paymentBook.getTotalCost());
                    existingBook.setImageVoucher(paymentBook.getImageVoucher());
                    existingBook.setTotalPoints(paymentBook.getTotalPoints());
                    existingBook.setPaymentComplete(paymentBook.getPaymentComplete());
                    return paymentBookRepository.save(existingBook);
                });
    }

    @Override
    public Mono<PaymentBookEntity> getPaymentBookById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    @Override
    public Flux<PaymentBookEntity> getAllPaymentBooks() {
        return paymentBookRepository.findAll();
    }

    @Override
    public Flux<PaymentBookEntity> getPaymentBooksByUserClientId(Integer userClientId) {
        return paymentBookRepository.findByUserClientId(userClientId);
    }

    @Override
    public Mono<Void> deletePaymentBook(Integer id) {
        return paymentBookRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateBookingStateIfRequired(Integer bookingId) {
        return bookingService.updateBookingStatePay(bookingId, 3) // Aquí verificas y actualizas el estado a 3
                .filter(booking -> booking.getBookingStateId() == 2) // Verifica si el estado es 2
                .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2)) // Si es 2, lo actualizas
                .then();
    }
    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }
}
