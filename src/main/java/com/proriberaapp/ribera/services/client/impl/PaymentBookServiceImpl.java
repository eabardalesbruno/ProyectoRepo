package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Domain.entities.PaymentBookEntity;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentBookRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentMethodRepository;
import com.proriberaapp.ribera.Infraestructure.repository.PaymentStateRepository;
import com.proriberaapp.ribera.Infraestructure.repository.UserClientRepository;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
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
    /*ANTES DEL CORREO
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
     */

    /*DESPUES DE CORREO
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingService bookingService;
    private final S3Uploader s3Uploader;
    private final EmailService emailService;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
                                  UserClientRepository userClientRepository,
                                  BookingService bookingService,
                                  S3Uploader s3Uploader,
                                  EmailService emailService) {
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
        this.emailService = emailService;
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
                .flatMap(savedPaymentBook -> {
                    return updateBookingStateIfRequired(savedPaymentBook.getBookingId())
                            .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
                                    .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook, userClient.getEmail()))
                            )
                            .thenReturn(savedPaymentBook);
                });
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

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email) {
        String emailBody = generatePaymentConfirmationEmailBody(paymentBook);
        return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
    }

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con éxito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
        body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }
     */
    private final PaymentBookRepository paymentBookRepository;
    private final UserClientRepository userClientRepository;
    private final BookingService bookingService;
    private final S3Uploader s3Uploader;
    private final EmailService emailService;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStateRepository paymentStateRepository;

    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
                                  UserClientRepository userClientRepository,
                                  BookingService bookingService,
                                  S3Uploader s3Uploader,
                                  EmailService emailService,
                                  PaymentMethodRepository paymentMethodRepository,
                                  PaymentStateRepository paymentStateRepository) {
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
        this.emailService = emailService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStateRepository = paymentStateRepository;
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
                .flatMap(savedPaymentBook -> updateBookingStateIfRequired(savedPaymentBook.getBookingId())
                        .then(userClientRepository.findById(savedPaymentBook.getUserClientId())
                                .flatMap(userClient -> sendPaymentConfirmationEmail(savedPaymentBook, userClient.getEmail()))
                        ).thenReturn(savedPaymentBook)
                );
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

    @Override
    public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails() {
        return paymentBookRepository.findAll()
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId())
                        ).map(tuple -> PaymentBookDetailsDTO.builder()
                                .paymentBookId(paymentBook.getPaymentBookId())
                                .bookingId(paymentBook.getBookingId())
                                .userClientId(paymentBook.getUserClientId())
                                .userClientName(tuple.getT2().getFirstName())
                                .bookingName(tuple.getT3().getDetail())
                                .paymentMethodId(paymentBook.getPaymentMethodId())
                                .paymentMethod(tuple.getT4().getDescription())
                                .paymentStateId(paymentBook.getPaymentStateId())
                                .paymentState(tuple.getT5().getPaymentStateName())
                                .refuseReasonId(paymentBook.getRefuseReasonId())
                                .paymentTypeId(paymentBook.getPaymentTypeId())
                                .paymentSubTypeId(paymentBook.getPaymentSubTypeId())
                                .currencyTypeId(paymentBook.getCurrencyTypeId())
                                .amount(paymentBook.getAmount())
                                .description(paymentBook.getDescription())
                                .paymentDate(paymentBook.getPaymentDate())
                                .operationCode(paymentBook.getOperationCode())
                                .note(paymentBook.getNote())
                                .totalCost(paymentBook.getTotalCost())
                                .imageVoucher(paymentBook.getImageVoucher())
                                .totalPoints(paymentBook.getTotalPoints())
                                .paymentComplete(paymentBook.getPaymentComplete())
                                .pendingPay(paymentBook.getPendingpay())
                                .build()
                        )
                );
    }

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email) {
        String emailBody = generatePaymentConfirmationEmailBody(paymentBook);
        return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
    }

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook) {
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<h1 style='text-align: center;'>Confirmación de Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con éxito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
        body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }
}
