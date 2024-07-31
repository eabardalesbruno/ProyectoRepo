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
        String body = "<html><head><title></title></head><body style='color:black'>";
        body += "<div style='width: 100%'>";
        body += "<div style='display:flex;'>";
        body += "</div>";
        body += "<img style='width: 100%' src='http://www.inresorts.club/Views/img/fondo.png'>";
        body += "<h1 style='text-align: center;'>Confirmacion de Pago</h1>";
        body += "<p>Estimado cliente,</p>";
        body += "<p>Su pago ha sido recibido con exito.</p>";
        body += "<p>Detalles del pago:</p>";
        body += "<ul>";
        body += "<li>Booking ID: " + paymentBook.getBookingId() + "</li>";
        body += "<li>Amount: " + paymentBook.getAmount() + "</li>";
        body += "<li>Payment Date: " + paymentBook.getPaymentDate() + "</li>";
        body += "</ul>";
        body += "<p>Gracias por su confianza.</p>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'></p>";
        body += "<center>Disfruta de nuestros servicios y promociones exclusivas.</center>";
        body += "</div></center>";
        body += "<center><div style='width: 100%'>";
        body += "<p style='margin-left: 10%; margin-right: 10%;'>-------------- o --------------</p>";
        body += "</div></center>";
        body += "</div></center>";
        body += "</body></html>";
        return body;
    }
}
