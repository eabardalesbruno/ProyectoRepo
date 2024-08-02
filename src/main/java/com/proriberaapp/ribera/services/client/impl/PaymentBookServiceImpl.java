package com.proriberaapp.ribera.services.client.impl;

import com.proriberaapp.ribera.Api.controllers.admin.dto.PaymentBookDetailsDTO;
import com.proriberaapp.ribera.Api.controllers.client.dto.PaginatedResponse;
import com.proriberaapp.ribera.Domain.entities.*;
import com.proriberaapp.ribera.Infraestructure.repository.*;
import com.proriberaapp.ribera.services.client.BookingService;
import com.proriberaapp.ribera.services.client.EmailService;
import com.proriberaapp.ribera.services.client.PaymentBookService;
import com.proriberaapp.ribera.services.client.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentSubtypeRepository paymentSubtypeRepository;

    private final CurrencyTypeRepository currencyTypeRepository;



    @Autowired
    public PaymentBookServiceImpl(PaymentBookRepository paymentBookRepository,
                                  UserClientRepository userClientRepository,
                                  BookingService bookingService,
                                  S3Uploader s3Uploader,
                                  EmailService emailService,
                                  PaymentMethodRepository paymentMethodRepository,
                                  PaymentStateRepository paymentStateRepository,PaymentTypeRepository paymentTypeRepository,PaymentSubtypeRepository paymentSubtypeRepository, CurrencyTypeRepository currencyTypeRepository) {
        this.paymentBookRepository = paymentBookRepository;
        this.userClientRepository = userClientRepository;
        this.bookingService = bookingService;
        this.s3Uploader = s3Uploader;
        this.emailService = emailService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStateRepository = paymentStateRepository;
        this.paymentTypeRepository = paymentTypeRepository;
        this.paymentSubtypeRepository = paymentSubtypeRepository;
        this.currencyTypeRepository = currencyTypeRepository;

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
        return bookingService.updateBookingStatePay(bookingId, 3)
                .filter(booking -> booking.getBookingStateId() == 2)
                .flatMap(booking -> bookingService.updateBookingStatePay(bookingId, 2))
                .then();
    }

    @Override
    public Mono<PaymentBookEntity> findById(Integer id) {
        return paymentBookRepository.findById(id);
    }

    /*
    @Override
    public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails() {
        return paymentBookRepository.findAll()
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                        ).map(tuple -> {
                            PaymentBookEntity paymentBookEntity = tuple.getT1();
                            UserClientEntity userClient = tuple.getT2();
                            BookingEntity booking = tuple.getT3();
                            PaymentMethodEntity paymentMethod = tuple.getT4();
                            PaymentStateEntity paymentState = tuple.getT5();
                            PaymentTypeEntity paymentType = tuple.getT6();
                            PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                            CurrencyTypeEntity currencyType = tuple.getT8();

                            PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                    .paymentBookId(paymentBookEntity.getPaymentBookId())
                                    .bookingId(paymentBookEntity.getBookingId())
                                    .userClientId(paymentBookEntity.getUserClientId())
                                    .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                    .paymentStateId(paymentBookEntity.getPaymentStateId())
                                    .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                    .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                    .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                    .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                    .amount(paymentBookEntity.getAmount())
                                    .description(paymentBookEntity.getDescription())
                                    .paymentDate(paymentBookEntity.getPaymentDate())
                                    .operationCode(paymentBookEntity.getOperationCode())
                                    .note(paymentBookEntity.getNote())
                                    .totalCost(paymentBookEntity.getTotalCost())
                                    .imageVoucher(paymentBookEntity.getImageVoucher())
                                    .totalPoints(paymentBookEntity.getTotalPoints())
                                    .paymentComplete(paymentBookEntity.getPaymentComplete())
                                    .pendingPay(paymentBookEntity.getPendingpay());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                            }
                            if (booking != null) {
                                builder.bookingName(booking.getDetail());
                            }
                            if (paymentMethod != null) {
                                builder.paymentMethod(paymentMethod.getDescription());
                            }
                            if (paymentState != null) {
                                builder.paymentState(paymentState.getPaymentStateName());
                            }
                            if (paymentType != null) {
                                builder.paymentType(paymentType.getPaymentTypeDesc());
                            }
                            if (paymentSubtype != null) {
                                builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                            }
                            if (currencyType != null) {
                                builder.currencyType(currencyType.getCurrencyTypeDescription());
                            }

                            return builder.build();
                        })
                );
    }
     */

    /*
    @Override
    public Flux<PaymentBookDetailsDTO> getAllPaymentBookDetails(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size, offset)
                .flatMap(paymentBook ->
                        Mono.zip(
                                Mono.just(paymentBook),
                                userClientRepository.findById(paymentBook.getUserClientId()),
                                bookingService.findById(paymentBook.getBookingId()),
                                paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                        ).map(tuple -> {
                            PaymentBookEntity paymentBookEntity = tuple.getT1();
                            UserClientEntity userClient = tuple.getT2();
                            BookingEntity booking = tuple.getT3();
                            PaymentMethodEntity paymentMethod = tuple.getT4();
                            PaymentStateEntity paymentState = tuple.getT5();
                            PaymentTypeEntity paymentType = tuple.getT6();
                            PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                            CurrencyTypeEntity currencyType = tuple.getT8();

                            PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                    .paymentBookId(paymentBookEntity.getPaymentBookId())
                                    .bookingId(paymentBookEntity.getBookingId())
                                    .userClientId(paymentBookEntity.getUserClientId())
                                    .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                    .paymentStateId(paymentBookEntity.getPaymentStateId())
                                    .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                    .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                    .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                    .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                    .amount(paymentBookEntity.getAmount())
                                    .description(paymentBookEntity.getDescription())
                                    .paymentDate(paymentBookEntity.getPaymentDate())
                                    .operationCode(paymentBookEntity.getOperationCode())
                                    .note(paymentBookEntity.getNote())
                                    .totalCost(paymentBookEntity.getTotalCost())
                                    .imageVoucher(paymentBookEntity.getImageVoucher())
                                    .totalPoints(paymentBookEntity.getTotalPoints())
                                    .paymentComplete(paymentBookEntity.getPaymentComplete())
                                    .pendingPay(paymentBookEntity.getPendingpay());

                            if (userClient != null) {
                                builder.userClientName(userClient.getFirstName());
                            }
                            if (booking != null) {
                                builder.bookingName(booking.getDetail());
                            }
                            if (paymentMethod != null) {
                                builder.paymentMethod(paymentMethod.getDescription());
                            }
                            if (paymentState != null) {
                                builder.paymentState(paymentState.getPaymentStateName());
                            }
                            if (paymentType != null) {
                                builder.paymentType(paymentType.getPaymentTypeDesc());
                            }
                            if (paymentSubtype != null) {
                                builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                            }
                            if (currencyType != null) {
                                builder.currencyType(currencyType.getCurrencyTypeDescription());
                            }

                            return builder.build();
                        })
                );
    }
     */

    @Override
    public Mono<PaginatedResponse<PaymentBookDetailsDTO>> getAllPaymentBookDetails(int page, int size) {
        int offset = page * size;
        return paymentBookRepository.countByRefuseReasonIdAndPendingPay(1, 0)
                .flatMap(totalElements -> paymentBookRepository.findAllByRefuseReasonIdAndPendingPay(1, 0, size, offset)
                        .flatMap(paymentBook ->
                                Mono.zip(
                                        Mono.just(paymentBook),
                                        userClientRepository.findById(paymentBook.getUserClientId()),
                                        bookingService.findById(paymentBook.getBookingId()),
                                        paymentMethodRepository.findById(paymentBook.getPaymentMethodId()),
                                        paymentStateRepository.findById(paymentBook.getPaymentStateId()),
                                        paymentTypeRepository.findById(paymentBook.getPaymentTypeId()),
                                        paymentSubtypeRepository.findById(paymentBook.getPaymentSubTypeId()),
                                        currencyTypeRepository.findById(paymentBook.getCurrencyTypeId())
                                ).map(tuple -> {
                                    PaymentBookEntity paymentBookEntity = tuple.getT1();
                                    UserClientEntity userClient = tuple.getT2();
                                    BookingEntity booking = tuple.getT3();
                                    PaymentMethodEntity paymentMethod = tuple.getT4();
                                    PaymentStateEntity paymentState = tuple.getT5();
                                    PaymentTypeEntity paymentType = tuple.getT6();
                                    PaymentSubtypeEntity paymentSubtype = tuple.getT7();
                                    CurrencyTypeEntity currencyType = tuple.getT8();

                                    PaymentBookDetailsDTO.PaymentBookDetailsDTOBuilder builder = PaymentBookDetailsDTO.builder()
                                            .paymentBookId(paymentBookEntity.getPaymentBookId())
                                            .bookingId(paymentBookEntity.getBookingId())
                                            .userClientId(paymentBookEntity.getUserClientId())
                                            .paymentMethodId(paymentBookEntity.getPaymentMethodId())
                                            .paymentStateId(paymentBookEntity.getPaymentStateId())
                                            .refuseReasonId(paymentBookEntity.getRefuseReasonId())
                                            .paymentTypeId(paymentBookEntity.getPaymentTypeId())
                                            .paymentSubTypeId(paymentBookEntity.getPaymentSubTypeId())
                                            .currencyTypeId(paymentBookEntity.getCurrencyTypeId())
                                            .amount(paymentBookEntity.getAmount())
                                            .description(paymentBookEntity.getDescription())
                                            .paymentDate(paymentBookEntity.getPaymentDate())
                                            .operationCode(paymentBookEntity.getOperationCode())
                                            .note(paymentBookEntity.getNote())
                                            .totalCost(paymentBookEntity.getTotalCost())
                                            .imageVoucher(paymentBookEntity.getImageVoucher())
                                            .totalPoints(paymentBookEntity.getTotalPoints())
                                            .paymentComplete(paymentBookEntity.getPaymentComplete())
                                            .pendingPay(paymentBookEntity.getPendingpay());

                                    if (userClient != null) {
                                        builder.userClientName(userClient.getFirstName());
                                    }
                                    if (booking != null) {
                                        builder.bookingName(booking.getDetail());
                                    }
                                    if (paymentMethod != null) {
                                        builder.paymentMethod(paymentMethod.getDescription());
                                    }
                                    if (paymentState != null) {
                                        builder.paymentState(paymentState.getPaymentStateName());
                                    }
                                    if (paymentType != null) {
                                        builder.paymentType(paymentType.getPaymentTypeDesc());
                                    }
                                    if (paymentSubtype != null) {
                                        builder.paymentSubtype(paymentSubtype.getPaymentSubtypeDesc());
                                    }
                                    if (currencyType != null) {
                                        builder.currencyType(currencyType.getCurrencyTypeDescription());
                                    }

                                    return builder.build();
                                })
                        )
                        .collectList()
                        .map(paymentBookDetails -> new PaginatedResponse<>(totalElements, paymentBookDetails)));
    }

    private Mono<Void> sendPaymentConfirmationEmail(PaymentBookEntity paymentBook, String email) {
        String emailBody = generatePaymentConfirmationEmailBody(paymentBook);
        return emailService.sendEmail(email, "Confirmación de Pago", emailBody);
    }

    /*
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

    private String generatePaymentConfirmationEmailBody(PaymentBookEntity paymentBook) {
        String body = "<html><head><title>Confirmación de Pago</title></head><body style='color:black'>";
        body += "<div style='width: 100%;'>";
        body += "<div style='display: flex; align-items: center; justify-content: space-between;'>";
        body += "<img style='width: 100px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453907774_2238863976459404_4409148998166454890_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=1p66qBQN6IYQ7kNvgEnxiv2&_nc_ht=scontent.flim1-2.fna&oh=00_AYACRHyTnMSMkClEmGFw8OmSBT2T_U4LGusY0F3KX0OBVQ&oe=66B1E966' alt='Logo Izquierda'>";
        body += "<div style='display: flex;'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453503393_2238863839792751_3678586622785113323_n.jpg?stp=cp0_dst-jpg&_nc_cat=108&ccb=1-7&_nc_sid=127cfc&_nc_ohc=OMKWsE877hcQ7kNvgHnzNGq&_nc_ht=scontent.flim1-2.fna&oh=00_AYBSmgM6SVV33fWdVeqn9sUMleFSdtOGZPcc0m-USS93bg&oe=66B20925' alt='Logo 1'>";
        body += "<img style='width: 80px; margin-right: 10px;' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453501437_2238863739792761_5553627034492335729_n.jpg?stp=cp0_dst-jpg&_nc_cat=111&ccb=1-7&_nc_sid=127cfc&_nc_ohc=fcEltLDDNeMQ7kNvgFNAsL6&_nc_ht=scontent.flim1-2.fna&oh=00_AYBD75zTjdsLuKmtk3vPYR7fBfCg5U2aVQ_tYm8679ZFCQ&oe=66B1FF76' alt='Logo 2'>";
        body += "<img style='width: 80px;' src='https://scontent.flim1-1.fna.fbcdn.net/v/t39.30808-6/453497633_2238863526459449_291281439279005519_n.jpg?stp=cp0_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=127cfc&_nc_ohc=vMzblHxFzGUQ7kNvgHhI3YO&_nc_ht=scontent.flim1-1.fna&oh=00_AYAEn_ThdeZSWqvo7RurNrnoAulbgxM7V5YzJc_CGsYACg&oe=66B1E905' alt='Logo 3'>";
        body += "</div>";
        body += "</div>";
        body += "<div style='display: flex; margin-top: 20px;'>";
        body += "<img style='width: 100%' src='https://scontent.flim1-2.fna.fbcdn.net/v/t39.30808-6/453514514_2238864093126059_4377276491425541120_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=127cfc&_nc_ohc=r0fzgelec-UQ7kNvgFL0EDI&_nc_ht=scontent.flim1-2.fna&oh=00_AYAJLos7io5zNmz08RwyK1pc5ZGwN5Cn8jt8Eg17N73CQQ&oe=66B1E807' alt='Banner'>";
        body += "</div>";
        body += "<h2 style='text-align: center;'>Confirmación de Pago</h2>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con éxito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Reserva: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Monto: " + paymentBook.getAmount() + "</li>";
        body += "<li>Fecha de pago: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "<div style='text-align: center;'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>Disfruta de nuestros servicios y promociones exclusivas.</p>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div>";
        body += "</body></html>";
        return body;
    }
}
